package com.prismworks.prism.learningtest.async;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@TestConfiguration
public class AsyncTestConfiguration {

    @Bean
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setMaxPoolSize(200);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setThreadNamePrefix("PRism Executor-");
        taskExecutor.initialize();

        return taskExecutor;
    }

    @Bean
    public AsyncServiceWithFuture asyncServiceWithFuture(Executor threadPoolTaskExecutor) {
        return new AsyncServiceWithFuture(threadPoolTaskExecutor);
    }

    @Bean
    public AsyncServiceWithSpringAsync asyncServiceWithSpringAsync() {
        return new AsyncServiceWithSpringAsync();
    }
}
