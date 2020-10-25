package org.kstrek.tweet.domain;

import org.junit.jupiter.api.Test;
import org.kstrek.tweet.adapters.InMemoryFollowingRepository;
import org.kstrek.tweet.adapters.InMemoryPostRepository;
import org.kstrek.tweet.domain.ports.PostRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostingServiceTest {

    @Test
    public void shouldReturnUserPostsInReverseChronoLogicalOrder() {
        var userId = new UserId("someUser");

        PostRepository postRepository = new InMemoryPostRepository();

        final Post newestPost = new Post("message1", Instant.now(), userId);
        final Post middlePost = new Post("message2", Instant.now().minusSeconds(10), userId);
        final Post oldestPost = new Post("message3", Instant.now().minusSeconds(120), userId);

        postRepository.newPost(oldestPost);
        postRepository.newPost(newestPost);
        postRepository.newPost(middlePost);

        final PostingService postingService = new PostingService(postRepository, new InMemoryFollowingRepository());

        final Collection<Post> resultingPostCollection = postingService.postsCreatedByUser(userId);

        assertThat(resultingPostCollection)
                .containsExactlyElementsOf(List.of(newestPost, middlePost, oldestPost));
    }

    @Test
    public void shouldReturnMergedCollectionOfPostsOfMultipleFolloweesInReverseChronologicalOrder() {
        var follower = new UserId("follower");
        var followee1 = new UserId("followee1");
        var followee2 = new UserId("followee2");

        PostRepository postRepository = new InMemoryPostRepository();

        final Post newestPost = new Post("message1", Instant.now(), followee1);
        final Post middlePost = new Post("message2", Instant.now().minusSeconds(10), followee2);
        final Post oldestPost = new Post("message3", Instant.now().minusSeconds(120), followee1);
        final Post followerPost = new Post("message4", Instant.now(), follower);

        postRepository.newPost(oldestPost);
        postRepository.newPost(newestPost);
        postRepository.newPost(middlePost);
        postRepository.newPost(followerPost);

        final InMemoryFollowingRepository followingRepository = new InMemoryFollowingRepository();
        followingRepository.addFollower(follower, followee1);
        followingRepository.addFollower(follower, followee2);

        final PostingService postingService = new PostingService(postRepository, followingRepository);

        final Collection<Post> resultingPostCollection = postingService.postsCreatedByFollowedUsers(follower);

        assertThat(resultingPostCollection)
                .containsExactlyElementsOf(List.of(newestPost, middlePost, oldestPost));
    }
}
