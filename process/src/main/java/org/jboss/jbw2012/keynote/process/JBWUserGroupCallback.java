package org.jboss.jbw2012.keynote.process;

import java.util.Arrays ;
import java.util.List ;

import javax.inject.Inject ;

import org.jboss.jbw2012.keynote.model.Team ;
import org.jboss.jbw2012.keynote.model.User ;
import org.jboss.jbw2012.keynote.model.utils.ModelUtils ;
import org.jbpm.task.service.UserGroupCallback ;

public class JBWUserGroupCallback implements UserGroupCallback
{
    @Inject
    private ModelUtils modelUtils ;

    @Override
    public boolean existsUser(final String userId)
    {
        if ("Administrator".equals(userId))
        {
            return true ;
        }
        else
        {
            try
            {
                final long id = Long.parseLong(userId) ;
                return (modelUtils.getUser(id) != null) ;
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
                final long id = Long.parseLong(userId) ;
                final User user = modelUtils.getUser(id)  ;
                if (user != null)
                {
                    return Arrays.asList(user.getTeam().name()) ;
                }
            }
            catch (final NumberFormatException nfe) {} // fall through
        }
        return null ;
    }
    
}
