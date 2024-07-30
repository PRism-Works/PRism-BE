package com.prismworks.prism.learningtest.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class AsyncServiceWithSpringAsync {

    @Async
    public CompletableFuture<Integer> asyncWithoutExecutor() {
        try {
            log.info("start asyncWithoutExecutor method");
            Thread.sleep(3000);
            log.info("end asyncWithoutExecutor method");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.completedFuture(1);
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Integer> asyncWithExecutor(boolean hasException) {
        try {
            log.info("start asyncWithExecutor method");
            Thread.sleep(3000);
            log.info("end asyncWithExecutor method");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(hasException) {
            throw new RuntimeException("indeed exception");
        }

        return CompletableFuture.completedFuture(1);
    }
}
