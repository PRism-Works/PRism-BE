package com.prismworks.prism.config;

import com.prismworks.prism.common.converter.ParamStringToEnumConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
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

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ParamStringToEnumConverter.ProcessMethodConverter());
        registry.addConverter(new ParamStringToEnumConverter.RecruitmentPositionConverter());
    }
}
