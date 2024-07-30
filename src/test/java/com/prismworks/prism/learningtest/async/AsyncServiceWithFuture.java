package com.prismworks.prism.learningtest.async;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@RequiredArgsConstructor
public class AsyncServiceWithFuture {

    private final Executor customExecutor;

    public void syncMethod() {
        String currentThreadName = Thread.currentThread().getName();
        log.info("[thread: {}] sync method start", currentThreadName);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("[thread: {}] method end", currentThreadName);
    }

    public CompletableFuture<Integer> asyncWithoutExecutor() {
        String mainThread = Thread.currentThread().getName();
        log.info("[thread: {}] asyncWithoutExecutor method start", mainThread);

        return CompletableFuture.supplyAsync(() -> {
            try {
                String asyncThread = Thread.currentThread().getName();
                log.info("[thread: {}] in future", asyncThread);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return 1;
        });
    }

    public CompletableFuture<Integer> asyncWithExecutor() {
        String mainThread = Thread.currentThread().getName();
        log.info("[thread: {}] asyncWithExecutor method start", mainThread);

        return CompletableFuture.supplyAsync(() -> {
            try {
                String asyncThread = Thread.currentThread().getName();
                log.info("[thread: {}] in future", asyncThread);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return 1;
        }, customExecutor);
    }
}
