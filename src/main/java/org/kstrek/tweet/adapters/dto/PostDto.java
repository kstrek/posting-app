package org.kstrek.tweet.adapters.dto;

import org.kstrek.tweet.domain.Post;

import java.time.Instant;

public class PostDto {

    private final String message;

    private final Instant createdAt;

    private final String createdBy;

    public PostDto(final String message, final Instant createdAt, final String createdBy) {
        this.message = message;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public static PostDto fromDomain(final Post post) {
        return new PostDto(post.getMessage(), post.getCreatedAt(), post.getCreatedBy().getValue());
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}
