package org.kstrek.tweet.domain;

import java.util.Objects;

public class UserId {
    final String value;

    public UserId(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be empty.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
