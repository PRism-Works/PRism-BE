package com.prismworks.prism.domain.auth.provider;

import com.prismworks.prism.domain.auth.dto.JwtTokenDto;
import com.prismworks.prism.domain.auth.model.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.prismworks.prism.utils.DateUtil.toLocalDateTime;

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

    public JwtTokenDto generateToken(String userId, Date issuedDate) {
        Date accessTokenExpiredDate = new Date(issuedDate.getTime() +  Long.parseLong(this.accessTokenExpiringMs));
        Date refreshTokenExpiredDate = new Date(issuedDate.getTime() + Long.parseLong(this.refreshTokenExpiringMs));

        return JwtTokenDto.builder()
                .accessToken(this.generateAccessToken(userId, issuedDate, accessTokenExpiredDate))
                .accessTokenExpiredAt(toLocalDateTime(accessTokenExpiredDate))
                .refreshToken(this.generateRefreshToken(issuedDate, refreshTokenExpiredDate))
                .refreshTokenExpiredAt(toLocalDateTime(refreshTokenExpiredDate))
                .build();
    }

    private String generateAccessToken(String userId, Date issuedAt, Date expiration) {
        return Jwts.builder()
                .claims()
                    .add("userId", userId)
//                    .add("role", "ROLE_USER") // todo: role 확장
                .and()
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    private String generateRefreshToken(Date issuedAt, Date expiration) {
        return Jwts.builder()
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claim = this.getClaim(token);
        String userId = claim.get("userId", String.class);
        UserContext userContext = (UserContext)userDetailsService.loadUserByUsername(userId);

        return new UsernamePasswordAuthenticationToken(userContext, userContext.getPassword(), userContext.getAuthorities());
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
