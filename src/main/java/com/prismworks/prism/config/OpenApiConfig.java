package com.prismworks.prism.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
            .title("PRism API DOCS")
            .version("v1")
            .description("PRism api 명세서");

        SecurityScheme jwtScheme = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .in(SecurityScheme.In.HEADER)
            .scheme("bearer")
            .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList("jwtAuth");

        Components components = new Components()
            .addSecuritySchemes("jwtAuth", jwtScheme);

        return new OpenAPI()
            .components(components)
            .addSecurityItem(securityRequirement)
            .info(info);
    }

    @Bean
    public GroupedOpenApi groupedAllApi() {
        String[] packagesToScan = {"com.prismworks.prism.domain"};

        return GroupedOpenApi.builder()
                .group("1. 전체")
                .packagesToScan(packagesToScan)
                .build();
    }

    @Bean
    public GroupedOpenApi groupedAuthOpenApi() {
        String[] packagesToScan = {"com.prismworks.prism.domain.auth"};

        return GroupedOpenApi.builder()
            .group("2. 인증")
            .packagesToScan(packagesToScan)
            .build();
    }

    @Bean
    public GroupedOpenApi groupedPeerReviewOpenApi() {
        return GroupedOpenApi.builder()
            .group("3. 동료평가")
            .pathsToMatch("/api/v1/auth/login", "/api/v1/peer-reviews/**")
            .build();
    }

    @Bean
    public GroupedOpenApi groupedPrismOpenApi() {
        return GroupedOpenApi.builder()
            .group("4. PRism 평가 및 분석 결과")
            .pathsToMatch("/api/v1/auth/login", "/api/v1/prism/**")
            .build();
    }

    @Bean
    public GroupedOpenApi groupedProjectOpenApi() {
        return GroupedOpenApi.builder()
            .group("5. 프로젝트")
            .pathsToMatch("/api/v1/auth/login", "/api/v1/projects/**")
            .build();
    }

    @Bean
    public GroupedOpenApi groupedSearchOpenApi() {
        String[] packagesToScan = {"com.prismworks.prism.domain.search"};

        return GroupedOpenApi.builder()
            .group("6. 검색")
            .packagesToScan(packagesToScan)
            .build();
    }

    @Bean
    public GroupedOpenApi groupedUserOpenApi() {
        return GroupedOpenApi.builder()
            .group("7. 사용자")
            .pathsToMatch("/api/v1/auth/login", "/api/v1/users/**")
            .build();
    }

    @Bean
    public GroupedOpenApi groupedPostOpenApi() {
        return GroupedOpenApi.builder()
            .group("8. 커뮤니티")
            .pathsToMatch("/api/v1/auth/login", "/api/v1/posts/**")
            .build();
    }
}