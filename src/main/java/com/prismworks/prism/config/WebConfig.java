package com.prismworks.prism.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        applicationContext.getBeansOfType(HandlerMethodArgumentResolver.class)
                .values()
                .forEach(argumentResolver -> {
                    if(argumentResolver.getClass().getPackageName().startsWith("com.prismworks")) {
                        resolvers.add(argumentResolver);
                    }
                });
    }

}
