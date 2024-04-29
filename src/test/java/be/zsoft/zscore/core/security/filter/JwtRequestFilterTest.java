package be.zsoft.zscore.core.security.filter;

import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.security.exception.TokenExpiredException;
import be.zsoft.zscore.core.security.exception.TokenInvalidException;
import be.zsoft.zscore.core.security.service.JwtService;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private PlayerService playerService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private Claims claims;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_optionsCall() throws Exception {
        when(request.getMethod()).thenReturn("OPTIONS");

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(jwtService, never()).getClaims(anyString());
        verify(userService, never()).getById(any(UUID.class));
    }

    @Test
    void doFilterInternal_noHeader() throws Exception {
        when(request.getMethod()).thenReturn("GET");

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(jwtService, never()).getClaims(anyString());
        verify(userService, never()).getById(any(UUID.class));
    }

    @Test
    void doFilterInternal_prefixWrong() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Api-Key 123");

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(jwtService, never()).getClaims(anyString());
        verify(userService, never()).getById(any(UUID.class));
    }

    @Test
    void doFilterInternal_expired() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer access_token");
        when(jwtService.getClaims("access_token")).thenThrow(ExpiredJwtException.class);

        assertThrows(TokenExpiredException.class, () -> jwtRequestFilter.doFilterInternal(request, response, chain));

        verify(chain, never()).doFilter(request, response);
        verify(jwtService).getClaims("access_token");
        verify(userService, never()).getById(any(UUID.class));
    }

    @Test
    void doFilterInternal_invalid() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer access_token");
        when(jwtService.getClaims("access_token")).thenThrow(IllegalArgumentException.class);

        assertThrows(TokenInvalidException.class, () -> jwtRequestFilter.doFilterInternal(request, response, chain));

        verify(chain, never()).doFilter(request, response);
        verify(jwtService).getClaims("access_token");
        verify(userService, never()).getById(any(UUID.class));
    }

    @Test
    void doFilterInternal_success_userToken() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer access_token");
        when(jwtService.getClaims("access_token")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1e7240eb-5fe1-4a13-a12f-fc6b9a511c97");
        when(jwtService.getTokenType(claims)).thenReturn(JwtService.TokenType.ACCESS);
        when(userService.getById(any(UUID.class))).thenReturn(User.builder().roles(List.of()).build());

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(jwtService).getClaims("access_token");
        verify(userService).getById(any(UUID.class));
        verify(playerService, never()).getPlayerById(any(UUID.class));

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_success_playerToken() throws Exception {
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn("Bearer access_token");
        when(jwtService.getClaims("access_token")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1e7240eb-5fe1-4a13-a12f-fc6b9a511c97");
        when(jwtService.getTokenType(claims)).thenReturn(JwtService.TokenType.PLAYER_ACCESS);
        when(playerService.getPlayerById(any(UUID.class))).thenReturn(Player.builder().game(Game.builder().id(UUID.randomUUID()).build()).build());

        jwtRequestFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(jwtService).getClaims("access_token");
        verify(userService, never()).getById(any(UUID.class));
        verify(playerService).getPlayerById(any(UUID.class));

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
}