package org.jboss.jbw2012.keynote.leaderboard.server;

import org.jboss.jbw2012.keynote.leaderboard.client.shared.ApprovalChange;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.qual.Both;

import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * @author Mike Brock
 */
public class InjectExposer {
  @Inject @Both Event<ApprovalChange> a;



}
