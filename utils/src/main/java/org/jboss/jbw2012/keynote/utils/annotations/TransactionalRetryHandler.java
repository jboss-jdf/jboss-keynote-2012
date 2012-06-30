package org.jboss.jbw2012.keynote.utils.annotations;

public interface TransactionalRetryHandler
{
    public boolean shouldRetry(final Throwable th) ;
}
