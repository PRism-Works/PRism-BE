package com.prismworks.prism.security.config;

import com.prismworks.prism.domain.auth.provider.JwtTokenProvider;
import com.prismworks.prism.domain.auth.service.AuthTokenBlackListService;
import com.prismworks.prism.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthTokenBlackListService authTokenBlackListService;
    private final AuthenticationEntryPoint entryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, authTokenBlackListService),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/healthcheck").permitAll()
                        .requestMatchers("/api/v1/prism/*","/api/v1/prism/*/*").permitAll()
                        .requestMatchers("/api/v1/auth/email/exists", "/api/v1/auth/code",
                                "/api/v1/auth/code/verification", "/api/v1/auth/signup", "/api/v1/auth/password",
                                "/api/v1/auth/refresh-token", "/api/v1/auth/login").permitAll()

                        .requestMatchers("/api/v1/users/*/profile").permitAll()

                        .requestMatchers("/api/v1/summary/by-name", "/api/v1/projects/summary/by-member-and-filters",
                                "/api/v1/projects/summary/detail/*",
                                "/api/v1/projects/who-involved-projects").permitAll()

                        .requestMatchers("/api/v1/search/projects").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/peer-reviews/link").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/peer-reviews/projects/*").permitAll()
                        .requestMatchers("/ai/call").permitAll() // todo: 제거

                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000", "https://prism-fe.vercel.app"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedMethods(List.of("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}