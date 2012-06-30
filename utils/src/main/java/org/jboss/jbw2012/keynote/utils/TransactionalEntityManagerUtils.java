package org.jboss.jbw2012.keynote.utils;

import java.util.concurrent.ConcurrentHashMap ;

import javax.annotation.Resource ;
import javax.persistence.EntityManager ;
import javax.persistence.EntityManagerFactory ;
import javax.persistence.FlushModeType ;
import javax.transaction.RollbackException ;
import javax.transaction.Synchronization ;
import javax.transaction.SystemException ;
import javax.transaction.Transaction ;
import javax.transaction.TransactionManager ;

public class TransactionalEntityManagerUtils
{
    @Resource(mappedName="java:/TransactionManager")
    private TransactionManager tm ;
    
    private ConcurrentHashMap<Transaction, EntityManagerSynchronization> txMap = new ConcurrentHashMap<Transaction, EntityManagerSynchronization>() ;
    
    public EntityManager getEntityManager(final EntityManagerFactory emf)
    {
        try
        {
            final Transaction tx = tm.getTransaction() ;
            if (tx == null)
            {
                throw new IllegalStateException("No encompassing transaction present") ;
            }
            EntityManagerSynchronization sync = txMap.get(tx) ;
            if (sync == null)
            {
                sync = new EntityManagerSynchronization(emf) ;
                txMap.put(tx, sync) ;
                tx.registerSynchronization(sync) ;
            }
            return sync.getEntityManager() ;
        }
        catch (final RollbackException re)
        {
            throw new IllegalStateException("Transaction management exception", re) ;
        }
        catch (final SystemException se)
        {
            throw new IllegalStateException("Transaction management exception", se) ;
        }
    }
    
    private class EntityManagerSynchronization implements Synchronization
    {
        private final EntityManager em ;
        
        public EntityManagerSynchronization(final EntityManagerFactory emf)
        {
            em = emf.createEntityManager() ;
            em.setFlushMode(FlushModeType.COMMIT) ;
        }
        
        public EntityManager getEntityManager()
        {
            return em ;
        }
        
        @Override
        public void beforeCompletion()
        {
            try
            {
                txMap.remove(tm.getTransaction()) ;
            }
            catch (final SystemException se)
            {
                throw new IllegalStateException("Transaction management exception", se) ;
            } 
        }

        @Override
        public void afterCompletion(int status)
        {
        }
    }
}
