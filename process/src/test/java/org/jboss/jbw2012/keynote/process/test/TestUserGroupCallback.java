package org.jboss.jbw2012.keynote.process.test;

import java.util.Arrays ;
import java.util.List ;

import org.jboss.jbw2012.keynote.model.Team ;
import org.jbpm.task.service.UserGroupCallback ;

public class TestUserGroupCallback implements UserGroupCallback
{

    @Override
    public boolean existsUser(final String userId)
    {
        if ("Administrator".equals(userId) || ("SUPERVISOR".equals(userId)))
        {
            return true ;
        }
        else
        {
            try
            {
                Long.parseLong(userId) ;
                return true ;
            }
            catch (final NumberFormatException nfe)
            {
                return false ;
            }
        }
    }

    @Override
    public boolean existsGroup(final String groupId)
    {
        return Team.valueOf(groupId) != null ;
    }

    @Override
    public List<String> getGroupsForUser(final String userId,
        final List<String> groupIds, final List<String> allExistingGroupIds)
    {
        if (userId != null)
        {
            try
            {
                Long.parseLong(userId) ;
                return Arrays.asList(Team.EAST.name()) ;
            }
            catch (final NumberFormatException nfe) {} // fall through
        }
        return null ;
    }
    
}
