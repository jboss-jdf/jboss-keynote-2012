package org.jboss.jbw2012.keynote.errai.events;

import java.util.List ;

public class UserUpdateEvent
{
  private final List<UserInfo> users;

  public UserUpdateEvent(final List<UserInfo> users)
  {
    this.users = users;
  }

  public List<UserInfo> getUsers()
  {
    return users;
  }
}
