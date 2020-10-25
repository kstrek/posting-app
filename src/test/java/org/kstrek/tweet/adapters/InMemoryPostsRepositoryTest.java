package org.kstrek.tweet.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kstrek.tweet.domain.Post;
import org.kstrek.tweet.domain.UserId;
import org.kstrek.tweet.domain.ports.PostRepository;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryPostsRepositoryTest {

    private PostRepository postRepository;

    @BeforeEach
    public void beforeEach() {
        postRepository = new InMemoryPostRepository();
    }

    @Test
    public void shouldStorePosts() {
        var user1 = new UserId("u1");
        var user2 = new UserId("u2");

        final Post post1 = Post.of("message1", user1);
        final Post post2 = Post.of("message2", user2);
        final Post post3 = Post.of("message3", user1);

        postRepository.newPost(post1);
        postRepository.newPost(post2);
        postRepository.newPost(post3);

        final Collection<Post> user1Posts =
                postRepository.postsCreatedByUser(user1);

        assertThat(user1Posts).containsExactly(post1, post3);
    }

    @Test
    public void shouldFallbackToEmptyCollectionIfNoPostsExistForUser() {
        final Collection<Post> posts = postRepository.postsCreatedByUser(new UserId("non-existent-id"));

        assertThat(posts).isEmpty();
    }

}
