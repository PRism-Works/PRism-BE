package com.prismworks.prism.security.provider;

import com.prismworks.prism.security.dto.JwtTokenDto;
import com.prismworks.prism.security.model.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${service.jwt.access-token-expire}")
    private String accessTokenExpiringMs;

    @Value("${service.jwt.refresh-token-expire}")
    private String refreshTokenExpiringMs;

    private final UserDetailsService userDetailsService;

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${service.jwt.secret}") String secretKey,
                            UserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtTokenDto generateToken(String userId, Date date) {
        return JwtTokenDto.builder()
                .accessToken(this.generateAccessToken(userId, date))
                .refreshToken(this.generateRefreshToken(date))
                .build();
    }

    public String generateAccessToken(String userId, Date date) {
        return Jwts.builder()
                .claims()
                    .add("userId", userId)
//                    .add("role", "ROLE_USER") // todo: role 확장
                .and()
                .issuedAt(date)
                .expiration(new Date(new Date().getTime() + Long.parseLong(this.accessTokenExpiringMs)))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Date date) {
        // todo: db에 저장
        return Jwts.builder()
                .issuedAt(date)
                .expiration(new Date(new Date().getTime() + Long.parseLong(this.refreshTokenExpiringMs)))
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claim = this.getClaim(token);
        String userId = claim.get("userId", String.class);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId); //todo: userContext만 authentication에 들어가게

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public void validateToken(String token) {
        Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims getClaim(String token) {
        return Jwts.parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token, Date date) {
        Date expiredDate = this.getClaim(token).getExpiration();
        return expiredDate.before(date);
    }
}
