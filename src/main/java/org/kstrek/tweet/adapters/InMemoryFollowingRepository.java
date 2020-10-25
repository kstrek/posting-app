package org.kstrek.tweet.adapters;

import org.kstrek.tweet.domain.UserId;
import org.kstrek.tweet.domain.ports.FollowingRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFollowingRepository implements FollowingRepository {

    final Map<UserId, Set<UserId>> followers = new ConcurrentHashMap<>();

    @Override
    public void addFollower(UserId follower, UserId followee) {
        followers.merge(follower, Set.of(followee),
                (oldFollowers, newFollower) -> {
                    var mergedSet = new HashSet<>(oldFollowers);
                    mergedSet.addAll(newFollower);
                    return mergedSet;
                });
    }

    @Override
    public Collection<UserId> getFollowedUsers(UserId userId) {
        return new ArrayList<>(followers.getOrDefault(userId, Set.of()));
    }
}
