package com.prismworks.prism.learningtest.async;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

@Slf4j
@SpringBootTest(classes = {AsyncTestConfiguration.class})
public class AsyncTest {

    @Autowired
    private AsyncServiceWithFuture asyncServiceWithFuture;

    @Autowired
    private AsyncServiceWithSpringAsync asyncServiceWithSpringAsync;

    @DisplayName("sync method")
    @Test
    public void syncMethod() {
        asyncServiceWithFuture.syncMethod();
    }

    @DisplayName("async without executor by future")
    @Test
    public void asyncFutureWithoutExecutor() {
        log.info("caller start");
        asyncServiceWithFuture.asyncWithoutExecutor();
        log.info("caller end");
    }

    @DisplayName("async with executor by future")
    @Test
    public void asyncFutureWithExecutor() {
        log.info("caller start");
        asyncServiceWithFuture.asyncWithExecutor();
        log.info("caller end");
    }

    @DisplayName("async without executor by spring async")
    @Test
    public void asyncWithoutExecutorBySpring() {
        log.info("caller start");

        CompletableFuture<Integer> completableFuture = asyncServiceWithSpringAsync.asyncWithoutExecutor();
        completableFuture.thenApply(result -> {
            log.info("future get result: {}", result);
            return null;
        });

        log.info("caller end");
    }

    @DisplayName("async with executor by spring async")
    @Test
    public void asyncWithExecutorBySpring() {
        log.info("caller start");

        CompletableFuture<Integer> completableFuture = asyncServiceWithSpringAsync.asyncWithExecutor(false);
        completableFuture.thenApply(result -> {
            log.info("future get result: {}", result);
            return null;
        });

        log.info("caller end");
    }

    @DisplayName("handle exception async method with executor by spring async")
    @Test
    public void handleExceptionAsyncWithoutExecutorBySpring() {
        log.info("caller start");

        CompletableFuture<Integer> completableFuture = asyncServiceWithSpringAsync.asyncWithExecutor(true);
        completableFuture.thenApply(result -> {
            log.info("future get result: {}", result);
            return null;
        }).exceptionally(e -> {
            log.error(e.getMessage());
            return null;
        });

        log.info("caller end");
    }

}
