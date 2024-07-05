package com.prismworks.prism.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi groupedAllApi() {
        String[] packagesToScan = {"com.prismworks.prism.domain"};

        return GroupedOpenApi.builder()
                .group("all")
                .packagesToScan(packagesToScan)
                .build();
    }
}
