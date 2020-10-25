package org.kstrek.tweet.domain;

import java.time.Instant;

public class Post {
    
    private final String message;

    private final Instant createdAt;

    private final UserId createdBy;

    private final static int MAX_MESSAGE_LENGTH = 140;

    public Post(final String message, final Instant createdAt, final UserId createdBy) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty.");
        }
        if (message.length() > MAX_MESSAGE_LENGTH) {
            throw new IllegalArgumentException("Message cannot be longer than " + MAX_MESSAGE_LENGTH + " characters.");
        }
        this.message = message;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public static Post of(final String message, final UserId createdBy) {
        return new Post(message, Instant.now(), createdBy);
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UserId getCreatedBy() {
        return createdBy;
    }
}
