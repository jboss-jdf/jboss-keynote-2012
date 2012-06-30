package org.jboss.jbw2012.keynote.utils.interceptor;

import java.lang.reflect.Method ;

import javax.inject.Inject ;
import javax.interceptor.AroundInvoke ;
import javax.interceptor.Interceptor ;
import javax.interceptor.InvocationContext ;
import javax.transaction.Status ;
import javax.transaction.UserTransaction ;

import org.jboss.jbw2012.keynote.utils.annotations.Transactional ;
import org.jboss.jbw2012.keynote.utils.annotations.TransactionalRetryHandler ;

@Transactional @Interceptor
public class TxInterceptor
{
    private static final int MAX_RETRY_COUNT = 5 ;
    
    private static final ThreadLocal<TransactionalRetryHandlerInfo[]> retryHandlers = new ThreadLocal<TransactionalRetryHandlerInfo[]>() ;
    
    @Inject
    private UserTransaction ut ;
    
    @AroundInvoke
    public Object handleTransaction(final InvocationContext ic)
        throws Throwable
    {
        final boolean handleTx = (ut.getStatus() == Status.STATUS_NO_TRANSACTION) ;
        if (!handleTx)
        {
            return ic.proceed() ;
        }
        else
        {
            int retryCount = 0 ;
            try
            {
                while(true)
                {
                    try
                    {
                        ut.begin() ;
                        try
                        {
                            return ic.proceed() ;
                        }
                        catch (final RuntimeException re)
                        {
                            if(ut.getStatus() == Status.STATUS_ACTIVE)
                            {
                                ut.setRollbackOnly() ;
                            }
                            throw re ;
                        }
                        catch (final Error error)
                        {
                            if(ut.getStatus() == Status.STATUS_ACTIVE)
                            {
                                ut.setRollbackOnly() ;
                            }
                            throw error ;
                        }
                        finally
                        {
                            final int status = ut.getStatus() ;
                            if (status == Status.STATUS_ACTIVE)
                            {
                                ut.commit() ;
                            }
                            else if (status == Status.STATUS_MARKED_ROLLBACK)
                            {
                                ut.rollback() ;
                            }
                        }
                    }
                    catch (final Throwable th)
                    {
                        if (retryCount++ < MAX_RETRY_COUNT)
                        {
                            final TransactionalRetryHandlerInfo[] retryHandlers = getRetryHandlers(ic) ;
                            boolean retry = false ;
                            for(TransactionalRetryHandlerInfo retryHandler: retryHandlers)
                            {
                                if (retryHandler.shouldRetry(th))
                                {
                                    retry = true ;
                                    break ;
                                }
                            }
                            if (!retry)
                            {
                                throw th ;
                            }
                        }
                        else
                        {
                            throw th ;
                        }
                    }
                }
            }
            finally
            {
                retryHandlers.set(null) ;
            }
        }
    }

    private static final TransactionalRetryHandlerInfo[] getRetryHandlers(final InvocationContext ic)
    {
        final TransactionalRetryHandlerInfo[] currentRetryHandlers = retryHandlers.get() ;
        if (currentRetryHandlers != null)
        {
            return currentRetryHandlers ;
        }
        else
        {
            final TransactionalRetryHandlerInfo[] newRetryHandlers ;
            final Method method = ic.getMethod()  ;
            final Transactional methodTransactional = method.getAnnotation(Transactional.class)  ;
            if (methodTransactional != null)
            {
                newRetryHandlers = getRetryHandlers(methodTransactional) ;
            }
            else
            {
                newRetryHandlers = getRetryHandlers(method.getDeclaringClass()) ;
            }
            retryHandlers.set(newRetryHandlers) ;
            return newRetryHandlers ;
        }
    }
    
    private static TransactionalRetryHandlerInfo[] getRetryHandlers(final Transactional transactional)
    {
        final Class<? extends TransactionalRetryHandler>[] retryHandlerClasses = transactional.retryHandlers() ;
        final int numRetryHandlers = retryHandlerClasses.length ;
        final TransactionalRetryHandlerInfo[] retryHandlers = new TransactionalRetryHandlerInfo[numRetryHandlers] ;
        for(int count = 0 ; count < numRetryHandlers ; count++)
        {
            retryHandlers[count] = new TransactionalRetryHandlerInfo(retryHandlerClasses[count]) ;
        }
        return retryHandlers ;
    }
    
    private static final TransactionalRetryHandlerInfo[] getRetryHandlers(final Class<?> clazz)
    {
        final Transactional classTransactional = clazz.getAnnotation(Transactional.class)  ;
        if (classTransactional == null)
        {
            final Class<?> superclass = clazz.getSuperclass() ;
            if (superclass != null)
            {
                return getRetryHandlers(superclass) ;
            }
            else
            {
                return null ;
            }
        }
        else
        {
            return getRetryHandlers(classTransactional) ;
        }
    }
}
