package org.kstrek.tweet.adapters;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ResponseContentTypeHandler;
import org.kstrek.tweet.adapters.dto.PostDto;
import org.kstrek.tweet.domain.Post;
import org.kstrek.tweet.domain.PostingService;
import org.kstrek.tweet.domain.UserId;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class WebApi {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private final PostingService postingService;

    private final static String USER_ID_PARAM_NAME = "userId";
    private final static String FOLLOWEE_ID_PARAM_NAME = "followeeId";
    private final static String MESSAGE_FIELD_NAME = "message";
    private final static String REASON_FIELD_NAME = "reason";

    public WebApi(final PostingService postingService) {
        this.postingService = postingService;
    }

    public Router router(final Vertx vertx) {
        final var router = Router.router(vertx);
        router.route("/*").produces("application/json").handler(ResponseContentTypeHandler.create());

        final var userPostsPath = "/users/:" + USER_ID_PARAM_NAME + "/posts";

        // GET /users/{userId}/posts
        router.get(userPostsPath)
                .handler(this::userPosts)
                .failureHandler(this::commonFailureHandler);

        // POST /users/{userId}/posts
        router.post(userPostsPath)
                .handler(BodyHandler.create())
                .handler(this::newPost)
                .failureHandler(this::commonFailureHandler);

        // GET /users/{userId}/followees/posts
        router.get("/users/:" + USER_ID_PARAM_NAME + "/followees/posts")
                .handler(this::allFollowedUsersPosts)
                .failureHandler(this::commonFailureHandler);

        // PUT /users/{userId}/followees/{followeeId}
        router.put("/users/:" + USER_ID_PARAM_NAME + "/followees/:" + FOLLOWEE_ID_PARAM_NAME)
                .handler(this::newFollowedUser)
                .failureHandler(this::commonFailureHandler);
        return router;
    }

    private void userPosts(final RoutingContext context) {
        final var userId = new UserId(context.pathParam(USER_ID_PARAM_NAME));
        final Collection<Post> posts = postingService.postsCreatedByUser(userId);

        context.response().end(serializedPostsArray(posts).encode());
    }

    private void newPost(final RoutingContext context) {
        final String message = context.getBodyAsJson().getString(MESSAGE_FIELD_NAME);
        final String userId = context.pathParam(USER_ID_PARAM_NAME);

        postingService.newPost(Post.of(message, new UserId(userId)));
        context.response().setStatusCode(201).end();
    }

    private void allFollowedUsersPosts(final RoutingContext context) {
        final var userId = new UserId(context.pathParam(USER_ID_PARAM_NAME));
        final Collection<Post> posts = postingService.postsCreatedByFollowedUsers(userId);

        context.response().end(serializedPostsArray(posts).encode());
    }

    private void newFollowedUser(final RoutingContext context) {
        final String userId = context.pathParam(USER_ID_PARAM_NAME);
        final String followeeId = context.pathParam(FOLLOWEE_ID_PARAM_NAME);

        postingService.addFollower(new UserId(userId), new UserId(followeeId));
        context.response().setStatusCode(200).end();
    }

    private JsonArray serializedPostsArray(final Collection<Post> posts) {
        final List<JsonObject> serializedPosts =
                posts.stream() // collection cannot be serialized directly into json array
                        .map(PostDto::fromDomain)
                        .map(JsonObject::mapFrom)
                        .collect(Collectors.toList());
        return new JsonArray(serializedPosts);
    }

    private void commonFailureHandler(final RoutingContext context) {
        if (context.failure() instanceof IllegalArgumentException) {
            final Map<String, Object> responseBody = Map.of(REASON_FIELD_NAME, context.failure().getMessage());
            context.response().setStatusCode(400).end(new JsonObject(responseBody).encode());
        } else {
            logger.log(Level.SEVERE, context.failure(), () -> "Failed to handle the request.");
            context.response().setStatusCode(500).end();
        }
    }
}
