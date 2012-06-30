package org.jboss.jbw2012.keynote.model.test.utils;

import java.util.List ;

import javax.inject.Inject ;
import javax.inject.Singleton ;
import javax.transaction.Status ;
import javax.transaction.UserTransaction ;

import junit.framework.Assert ;

import org.jboss.jbw2012.keynote.model.Category ;
import org.jboss.jbw2012.keynote.model.Item ;
import org.jboss.jbw2012.keynote.model.User ;

@Singleton
public class ModelTestUtils
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

    public static void compareUsers(final String message, final List<User> lhs,
        final List<User> rhs)
    {
        if ((lhs == null) || (rhs == null))
        {
            Assert.assertEquals(message, lhs, rhs) ;
        }
        else
        {
            Assert.assertEquals(message, lhs.size(), rhs.size()) ;
            for(int count = 0 ; count < lhs.size() ; count++)
            {
                compareUser(message, lhs.get(count), rhs.get(count)) ;
            }
        }
    }

    public static void compareUser(final String message, final User lhs, final User rhs)
    {
        Assert.assertEquals(message, lhs.getId(), rhs.getId()) ;
        Assert.assertEquals(message, lhs.getName(), rhs.getName()) ;
        Assert.assertEquals(message, lhs.getTeam(), rhs.getTeam()) ;
        Assert.assertEquals(message, lhs.getRole(), rhs.getRole()) ;
        Assert.assertEquals(message, lhs.getCustomerStatus(), rhs.getCustomerStatus()) ;
        Assert.assertEquals(message, lhs.isVip(), rhs.isVip()) ;
    }

    public static void compareCategories(final String message, final List<Category> lhs,
        final List<Category> rhs)
    {
        if ((lhs == null) || (rhs == null))
        {
            Assert.assertEquals(message, lhs, rhs) ;
        }
        else
        {
            Assert.assertEquals(message, lhs.size(), rhs.size()) ;
            for(int count = 0 ; count < lhs.size() ; count++)
            {
                compareCategory(message, lhs.get(count), rhs.get(count)) ;
            }
        }
    }

    public static void compareCategory(final String message, final Category lhs, final Category rhs)
    {
        Assert.assertEquals(message, lhs.getId(), rhs.getId()) ;
        Assert.assertEquals(message, lhs.getName(), rhs.getName()) ;
        compareItems(message, lhs.getItems(), rhs.getItems()) ;
    }

    public static void compareItems(final String message, final List<Item> lhs,
        final List<Item> rhs)
    {
        if ((lhs == null) || (rhs == null))
        {
            Assert.assertEquals(message, lhs, rhs) ;
        }
        else
        {
            Assert.assertEquals(message, lhs.size(), rhs.size()) ;
            for(int count = 0 ; count < lhs.size() ; count++)
            {
                compareItem(message, lhs.get(count), rhs.get(count)) ;
            }
        }
    }
    
    public static void compareItem(final String message, final Item lhs, final Item rhs)
    {
        Assert.assertEquals(message, lhs.getId(), rhs.getId()) ;
        Assert.assertEquals(message, lhs.getName(), rhs.getName()) ;
        Assert.assertEquals(message, lhs.getImageURL(), rhs.getImageURL()) ;
        Assert.assertEquals(message, lhs.getThumbnailURL(), rhs.getThumbnailURL()) ;
        Assert.assertEquals(message, lhs.getDescription(), rhs.getDescription()) ;
        Assert.assertEquals(message, lhs.getPrice(), rhs.getPrice()) ;
    }
}
