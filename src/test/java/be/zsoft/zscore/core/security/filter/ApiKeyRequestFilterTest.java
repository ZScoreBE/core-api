package be.zsoft.zscore.core.security.filter;

import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.user.Role;
import be.zsoft.zscore.core.security.dto.AuthenticationData;
import be.zsoft.zscore.core.security.dto.ZScoreAuthenticationToken;
import be.zsoft.zscore.core.service.game.GameService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiKeyRequestFilterTest {

    @Mock
    private GameService gameService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @InjectMocks
    private ApiKeyRequestFilter filter;

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_shouldNotProcessWhenOptionsCall() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("ApiKey the_key");
        when(request.getMethod()).thenReturn("OPTIONS");

        filter.doFilterInternal(request, response, chain);

        verify(gameService, never()).getGameByApiKey(anyString());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotProcessWhenAuthHeaderBlank() throws Exception {
        when(request.getMethod()).thenReturn("GET");

        filter.doFilterInternal(request, response, chain);

        verify(gameService, never()).getGameByApiKey(anyString());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotProcessWhenAuthHeaderNotStartingWithPrefix() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("WrongPrefix the_key");
        when(request.getMethod()).thenReturn("GET");

        filter.doFilterInternal(request, response, chain);

        verify(gameService, never()).getGameByApiKey(anyString());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotProcessWhenSecurityContextIsNotEmpty() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(new ZScoreAuthenticationToken(null, "the_key", List.of()));

        when(request.getHeader("Authorization")).thenReturn("ApiKey the_key");
        when(request.getMethod()).thenReturn("GET");

        filter.doFilterInternal(request, response, chain);

        verify(gameService, never()).getGameByApiKey(anyString());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_process() throws Exception {
        Organization organization = Organization.builder().id(UUID.randomUUID()).build();
        Game game = Game.builder()
                .id(UUID.randomUUID())
                .organization(organization)
                .build();

        when(request.getHeader("Authorization")).thenReturn("ApiKey the_key");
        when(request.getMethod()).thenReturn("GET");
        when(gameService.getGameByApiKey("the_key")).thenReturn(game);

        filter.doFilterInternal(request, response, chain);

        verify(gameService).getGameByApiKey("the_key");
        verify(chain).doFilter(request, response);

        ZScoreAuthenticationToken expected = new ZScoreAuthenticationToken(
                new AuthenticationData(null, null, game, organization),
                "the_key",
                List.of(new SimpleGrantedAuthority(Role.ROLE_API.name()))
        );
        expected.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        assertEquals(expected, SecurityContextHolder.getContext().getAuthentication());
    }
}