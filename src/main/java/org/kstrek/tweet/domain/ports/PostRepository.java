package org.kstrek.tweet.domain.ports;

import org.kstrek.tweet.domain.Post;
import org.kstrek.tweet.domain.UserId;

import java.util.Collection;

public interface PostRepository {
   void newPost(final Post post);
   Collection<Post> postsCreatedByUser(final UserId userId);
}
