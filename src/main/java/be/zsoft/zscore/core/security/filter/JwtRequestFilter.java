package be.zsoft.zscore.core.security.filter;

import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.security.dto.AuthenticationData;
import be.zsoft.zscore.core.security.dto.ZScoreAuthenticationToken;
import be.zsoft.zscore.core.security.exception.TokenExpiredException;
import be.zsoft.zscore.core.security.exception.TokenInvalidException;
import be.zsoft.zscore.core.security.service.JwtService;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String OPTIONS_HTTP_VERB = "OPTIONS";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final UserService userService;
    private final PlayerService playerService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String tokenHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (shouldNotProcess(request, tokenHeader)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String rawToken = tokenHeader.replace(BEARER_PREFIX, "");
            Claims tokenClaims = jwtService.getClaims(rawToken);
            String id = tokenClaims.getSubject();
            JwtService.TokenType type = jwtService.getTokenType(tokenClaims);

            if (id != null) {
                UsernamePasswordAuthenticationToken authToken = switch (type) {
                    case ACCESS -> handleUserToken(UUID.fromString(id), rawToken);
                    case PLAYER_ACCESS -> handlePlayerToken(UUID.fromString(id), rawToken);
                    default -> null;
                };

                if (authToken != null) {
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("JWT Token has expired");
        } catch (IllegalArgumentException | MalformedJwtException e) {
            throw new TokenInvalidException("Invalid JWT Token");
        }

        chain.doFilter(request, response);
    }

    private boolean shouldNotProcess(HttpServletRequest request, String authHeader) {
        return request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_VERB) ||
                StringUtils.isBlank(authHeader) ||
                !authHeader.startsWith(BEARER_PREFIX) ||
                SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private ZScoreAuthenticationToken handleUserToken(UUID id, String rawToken) {
        User user = userService.getById(id);
        return new ZScoreAuthenticationToken(
                new AuthenticationData(user, null, null, user.getOrganization()),
                rawToken,
                user.getAuthorities()
        );
    }

    private ZScoreAuthenticationToken handlePlayerToken(UUID id, String rawToken) {
        Player player = playerService.getPlayerById(id);
        return new ZScoreAuthenticationToken(
                new AuthenticationData(null, player, player.getGame(), null),
                rawToken,
                player.getAuthorities()
        );
    }
}
