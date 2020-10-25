package org.kstrek.tweet.adapters;

import org.kstrek.tweet.domain.Post;
import org.kstrek.tweet.domain.UserId;
import org.kstrek.tweet.domain.ports.PostRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryPostRepository implements PostRepository {

    final Map<UserId, Collection<Post>> userPosts = new ConcurrentHashMap<>();

    @Override
    public void newPost(final Post post) {
        userPosts.merge(post.getCreatedBy(), List.of(post),
                (oldPosts, newPost) -> {
                    var mergedList = new ArrayList<>(oldPosts);
                    mergedList.addAll(newPost);
                    return mergedList;
                });
    }

    @Override
    public Collection<Post> postsCreatedByUser(final UserId userId) {
        return new ArrayList<>(userPosts.getOrDefault(userId, List.of()));
    }
}
