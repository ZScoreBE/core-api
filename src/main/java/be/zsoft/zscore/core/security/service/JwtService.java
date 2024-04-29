package be.zsoft.zscore.core.security.service;

import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.security.properties.TokenProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtService {

    public enum TokenType {
        ACCESS, PLAYER_ACCESS, REFRESH, PLAYER_REFRESH;
    }

    private final TokenProperties tokenProperties;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(tokenProperties.getSecret().getBytes());
    }

    public String generateToken(User user, TokenType type) {
        return generateToken(
                user.getId().toString(),
                user.getAuthorities(),
                type
        );
    }

    public String generateToken(String subject, Collection<? extends GrantedAuthority> authorities, TokenType type) {
        log.debug("Generating {} token for subject: {}", type, subject);

        return Jwts.builder()
                .subject(subject)
                .claims(createClaims(authorities.stream().map(GrantedAuthority::getAuthority).toList(), type))
                .expiration(Date.from(Instant.now().plus(getExpiryDuration(type), ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
    }

    public boolean isRefreshToken(String token) {
        Claims claims = getClaims(token);
        TokenType type = getTokenType(claims);
        return type == TokenType.REFRESH || type == TokenType.PLAYER_REFRESH;
    }

    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public TokenType getTokenType(Claims claims) {
        if (!claims.containsKey("type")) {
            return null;
        }

        var typeName = claims.get("type", String.class);
        return EnumUtils.getEnum(TokenType.class, typeName);
    }

    private Map<String, ?> createClaims(List<String> roles, TokenType type) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", type);

        if (type == TokenType.ACCESS || type == TokenType.PLAYER_ACCESS) {
            claims.put("roles", roles);
        }

        return claims;
    }

    private int getExpiryDuration(TokenType type) {
        return switch (type) {
            case ACCESS -> tokenProperties.getTokenExpiryDuration();
            case REFRESH -> tokenProperties.getRefreshTokenExpiryDuration();
            case PLAYER_ACCESS -> tokenProperties.getPlayerTokenExpiryDuration();
            case PLAYER_REFRESH -> tokenProperties.getPlayerRefreshTokenExpiryDuration();
        };
    }
}
