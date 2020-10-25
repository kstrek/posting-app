package org.kstrek.tweet.domain.ports;

import org.kstrek.tweet.domain.UserId;

import java.util.Collection;

public interface FollowingRepository {
    void addFollower(final UserId follower, final UserId followee);
    Collection<UserId> getFollowedUsers(final UserId userId);
}
