package org.kstrek.tweet.domain;

import org.kstrek.tweet.domain.ports.FollowingRepository;
import org.kstrek.tweet.domain.ports.PostRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class PostingService {

    final PostRepository postRepository;

    final FollowingRepository followingRepository;

    final static Comparator<Post> POST_REVERSE_CHRONOLOGICAL_ORDER_COMPARATOR =
            Comparator.comparingLong(post -> post.getCreatedAt().getNano());

    public PostingService(PostRepository postRepository, FollowingRepository followingRepository) {
        this.postRepository = postRepository;
        this.followingRepository = followingRepository;
    }

    public final void newPost(final Post post) {
        postRepository.newPost(post);
    }

    public final Collection<Post> postsCreatedByUser(final UserId userId) {
        return postRepository.postsCreatedByUser(userId)
                .stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(POST_REVERSE_CHRONOLOGICAL_ORDER_COMPARATOR)));
    }

    public final Collection<Post> postsCreatedByFollowedUsers(final UserId userId) {
        var followedUsers = followingRepository.getFollowedUsers(userId);

        return followedUsers
                .stream()
                .map(postRepository::postsCreatedByUser)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(() -> new TreeSet<>(POST_REVERSE_CHRONOLOGICAL_ORDER_COMPARATOR)));
    }

    public void addFollower(final UserId follower, final UserId followed) {
        followingRepository.addFollower(follower, followed);
    }
}
