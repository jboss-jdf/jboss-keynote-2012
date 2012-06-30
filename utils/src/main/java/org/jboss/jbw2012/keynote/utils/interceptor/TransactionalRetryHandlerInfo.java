package org.jboss.jbw2012.keynote.utils.interceptor;

import org.jboss.jbw2012.keynote.utils.annotations.TransactionalRetryHandler ;

public class TransactionalRetryHandlerInfo
{
    private final Class<? extends TransactionalRetryHandler> clazz ;
    private TransactionalRetryHandler handler ;
    
    public TransactionalRetryHandlerInfo(final Class<? extends TransactionalRetryHandler> clazz)
    {
        this.clazz = clazz ;
    }

    public boolean shouldRetry(final Throwable th)
    {
        if (handler == null)
        {
            try
            {
                handler = clazz.newInstance() ;
            }
            catch (final Throwable th2)
            {
                // KEV log error
                return false ;
            }
        }
        return handler.shouldRetry(th) ;
    }
}
