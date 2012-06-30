package org.jboss.jbw2012.keynote.leaderboard.server;


import org.jboss.jbw2012.keynote.leaderboard.client.shared.Role;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.Team;
import org.jboss.jbw2012.keynote.leaderboard.client.shared.User;

import java.util.Random;

/**
 * @author Mike Brock
 */
public final class FakeFactory {
  private FakeFactory() {
  }

  public static User newFakeUser() {
    return new User(random.nextLong(), randomName(), randomRole(), randomTeam());
  }

  private static final char[] nameChars = {
          'a', 'b', 'c', 'd', 'e', 'f',
          'g', 'h', 'i', 'j', 'k', 'l',
          'm', 'n', 'o', 'p', 'q', 'r',
          's', 't', 'u', 'v', 'w', 'x',
          'y', 'z'};

  public static String randomName() {
    final char[] nameArray = new char[12];
    for (int i = 0; i < nameArray.length; i++) {
      nameArray[i] = nameChars[nextUnsignedInt() % nameChars.length];
    }
    return new String(nameArray);
  }

  public static Role randomRole() {
    return Role.values()[nextUnsignedInt() % Role.values().length];
  }

  public static Team randomTeam() {
    switch (nextUnsignedInt() % 2) {
      case 0:
        return Team.EAST;
      case 1:
      default:
        return Team.WEST;
    }
  }

  private final static Random random = new Random(System.nanoTime() * 31);

  public static int nextUnsignedInt() {
    int nextInt = random.nextInt();
    if (nextInt < 0) {
      nextInt = -nextInt;
    }
    return nextInt;
  }

  public static double nextUnsignedDouble() {
    double nextDouble = random.nextDouble();
    if (nextDouble < 0) {
      nextDouble = -nextDouble;
    }
    return nextDouble;
  }
}
