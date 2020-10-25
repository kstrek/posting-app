package org.kstrek.tweet.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kstrek.tweet.domain.UserId;
import org.kstrek.tweet.domain.ports.FollowingRepository;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryFollowingRepositoryTest {

    private FollowingRepository followingRepository;

    @BeforeEach
    public void beforeEach() {
        followingRepository = new InMemoryFollowingRepository();
    }

    @Test
    public void shouldFallbackToEmptyCollectionIfNoFolloweesDefinedForUser() {
        final Collection<UserId> followees = followingRepository.getFollowedUsers(new UserId("non-existent-id"));

        assertThat(followees).isEmpty();
    }

    @Test
    public void shouldNotStoreDuplicatedFollowings() {
        var follower = new UserId("follower");
        var followee = new UserId("followee");

        followingRepository.addFollower(follower, followee);
        followingRepository.addFollower(follower, followee);

        final Collection<UserId> result = followingRepository.getFollowedUsers(follower);

        assertThat(result).singleElement().isEqualTo(followee);
    }
}
