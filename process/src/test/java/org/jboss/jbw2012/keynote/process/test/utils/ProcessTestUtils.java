package org.jboss.jbw2012.keynote.process.test.utils;

import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.transaction.Status ;
import javax.transaction.UserTransaction ;

@Singleton
public class ProcessTestUtils
{
    @Inject
    private UserTransaction ut ;
    
    public void startTransaction()
        throws Exception
    {
        ut.begin() ;
    }
    
    public void endTransaction(final boolean rollback)
        throws Exception
    {
        if (rollback || (ut.getStatus() != Status.STATUS_ACTIVE))
        {
            ut.rollback() ;
        }
        else
        {
            ut.commit() ;
        }
    }
}
