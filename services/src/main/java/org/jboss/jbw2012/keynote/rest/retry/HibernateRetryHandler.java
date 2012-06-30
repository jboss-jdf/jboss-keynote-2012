package org.jboss.jbw2012.keynote.rest.retry;

import javax.persistence.OptimisticLockException ;

import org.hibernate.exception.ConstraintViolationException ;
import org.jboss.jbw2012.keynote.utils.annotations.TransactionalRetryHandler ;

public class HibernateRetryHandler implements TransactionalRetryHandler
{
    @Override
    public boolean shouldRetry(Throwable th)
    {
        while(th != null)
        {
            if ((th instanceof OptimisticLockException) || (th instanceof ConstraintViolationException))
            {
                return true ;
            }
            final Throwable cause = th.getCause() ;
            th = (th == cause ? null : cause) ;
        }
        return false ;
    }
}
