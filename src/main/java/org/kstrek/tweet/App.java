package org.kstrek.tweet;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.kstrek.tweet.adapters.InMemoryFollowingRepository;
import org.kstrek.tweet.adapters.InMemoryPostRepository;
import org.kstrek.tweet.adapters.WebApi;
import org.kstrek.tweet.domain.PostingService;

public class App {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        final PostingService postingService =
                new PostingService(new InMemoryPostRepository(), new InMemoryFollowingRepository());

        final Router router = new WebApi(postingService).router(vertx);

        server.requestHandler(router).listen(8080);
    }
}
