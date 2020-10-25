package org.kstrek.tweet.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PostTest {

    @Test
    public void shouldFailToConstructPostWithMessageLongerThanAllowed() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Post.of("a".repeat(200), new UserId("someUser")));
    }

    @Test
    public void shouldFailToConstructPostWithEmptyMessage() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Post.of("", new UserId("someUser")));
    }
}
