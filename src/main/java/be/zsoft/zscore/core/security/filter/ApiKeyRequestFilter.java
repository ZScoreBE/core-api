package be.zsoft.zscore.core.security.filter;

import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.user.Role;
import be.zsoft.zscore.core.security.dto.AuthenticationData;
import be.zsoft.zscore.core.security.dto.ZScoreAuthenticationToken;
import be.zsoft.zscore.core.service.game.GameService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ApiKeyRequestFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String OPTIONS_HTTP_VERB = "OPTIONS";
    private static final String API_KEY_PREFIX = "ApiKey ";

    private final GameService gameService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (shouldNotProcess(request, authHeader)) {
            chain.doFilter(request, response);
            return;
        }

        String rawKey = authHeader.replace(API_KEY_PREFIX, "");
        Game game = gameService.getGameByApiKey(rawKey);

        AbstractAuthenticationToken authToken = new ZScoreAuthenticationToken(
                new AuthenticationData(null, null, game, game.getOrganization()),
                rawKey,
                List.of(new SimpleGrantedAuthority(Role.ROLE_API.name()))
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        chain.doFilter(request, response);
    }

    private boolean shouldNotProcess(HttpServletRequest request, String authHeader) {
        return request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_VERB) ||
                StringUtils.isBlank(authHeader) ||
                !authHeader.startsWith(API_KEY_PREFIX) ||
                SecurityContextHolder.getContext().getAuthentication() != null;
    }
}
