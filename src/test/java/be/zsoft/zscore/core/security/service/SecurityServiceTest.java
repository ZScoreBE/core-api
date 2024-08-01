package be.zsoft.zscore.core.security.service;

import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.user.Role;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.user.UserFixture;
import be.zsoft.zscore.core.security.dto.request.TokenForPlayerRequest;
import be.zsoft.zscore.core.security.dto.request.TokenRequest;
import be.zsoft.zscore.core.security.dto.response.TokenResponse;
import be.zsoft.zscore.core.security.exception.TokenExpiredException;
import be.zsoft.zscore.core.security.exception.TokenInvalidException;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.user.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PlayerService playerService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Clock clock;

    @InjectMocks
    private SecurityService securityService;

    @Captor
    private ArgumentCaptor<UsernamePasswordAuthenticationToken> authTokenCaptor;

    @Captor
    private ArgumentCaptor<Player> playerCaptor;

    @Test
    void getToken_shouldReturnTokens() {
        Authentication mockedAuth = mock(Authentication.class);
        User user = UserFixture.aDefaultUser();

        when(authenticationManager.authenticate(authTokenCaptor.capture())).thenReturn(mockedAuth);
        when(mockedAuth.getName()).thenReturn("wout@z-soft.be");
        when(userService.loadUserByUsername("wout@z-soft.be")).thenReturn(user);
        when(jwtService.generateToken(user, JwtService.TokenType.ACCESS)).thenReturn("access_token");
        when(jwtService.generateToken(user, JwtService.TokenType.REFRESH)).thenReturn("refresh_token");

        TokenResponse result = securityService.getToken(new TokenRequest("wout@z-soft.be", "pass"));

        assertEquals("wout@z-soft.be", authTokenCaptor.getValue().getName());
        assertEquals("pass", authTokenCaptor.getValue().getCredentials());

        TokenResponse expected = new TokenResponse("access_token", "refresh_token");
        assertEquals(expected, result);
    }

    @Test
    void getToken_shouldThrowExceptionIfNotActivated() {
        Authentication mockedAuth = mock(Authentication.class);
        User user = UserFixture.aNotActivatedUser();

        when(authenticationManager.authenticate(authTokenCaptor.capture())).thenReturn(mockedAuth);
        when(mockedAuth.getName()).thenReturn("wout@z-soft.be");
        when(userService.loadUserByUsername("wout@z-soft.be")).thenReturn(user);

        assertThrows(ApiException.class, () -> securityService.getToken(new TokenRequest("wout@z-soft.be", "pass")));

        assertEquals("wout@z-soft.be", authTokenCaptor.getValue().getName());
        assertEquals("pass", authTokenCaptor.getValue().getCredentials());
    }

    @Test
    void getToken_forPlayer() {
        Player player = PlayerFixture.aDefaultPlayer();
        UUID id = player.getId();

        when(playerService.getPlayerById(id)).thenReturn(player);
        when(jwtService.generateToken(id.toString(), List.of(new SimpleGrantedAuthority(Role.ROLE_PLAYER.name())), JwtService.TokenType.PLAYER_ACCESS)).thenReturn("access_token");
        when(jwtService.generateToken(id.toString(), List.of(new SimpleGrantedAuthority(Role.ROLE_PLAYER.name())), JwtService.TokenType.PLAYER_REFRESH)).thenReturn("refresh_token");
        mockLocalDateTimeNow();

        TokenResponse result = securityService.getToken(new TokenForPlayerRequest(id));

        assertEquals("access_token", result.accessToken());
        assertEquals("refresh_token", result.refreshToken());

        verify(playerService).getPlayerById(id);
        verify(playerService).rawUpdate(playerCaptor.capture());
        verify(jwtService).generateToken(id.toString(), List.of(new SimpleGrantedAuthority(Role.ROLE_PLAYER.name())), JwtService.TokenType.PLAYER_ACCESS);
        verify(jwtService).generateToken(id.toString(), List.of(new SimpleGrantedAuthority(Role.ROLE_PLAYER.name())), JwtService.TokenType.PLAYER_REFRESH);

        assertEquals(LocalDateTime.of(2000, 1, 1, 10, 30), player.getLastSignIn());
    }

    @Test
    void refreshToken_shouldReturnTokens() {
        UUID id = UUID.randomUUID();
        User user = UserFixture.aDefaultUser();

        when(jwtService.isRefreshToken("refresh_token")).thenReturn(true);
        when(jwtService.getSubject("refresh_token")).thenReturn(id.toString());
        when(userService.getById(id)).thenReturn(user);
        when(jwtService.generateToken(user, JwtService.TokenType.ACCESS)).thenReturn("access_token");
        when(jwtService.generateToken(user, JwtService.TokenType.REFRESH)).thenReturn("refresh_token");

        TokenResponse result = securityService.refreshToken("refresh_token", false);
        TokenResponse expected = new TokenResponse("access_token", "refresh_token");

        assertEquals(expected, result);
    }

    @Test
    void refreshToken_shouldReturnTokensForPlayer() {
        Player player = PlayerFixture.aDefaultPlayer();
        UUID id = player.getId();

        when(jwtService.isRefreshToken("refresh_token")).thenReturn(true);
        when(jwtService.getSubject("refresh_token")).thenReturn(id.toString());
        when(playerService.getPlayerById(id)).thenReturn(player);
        when(jwtService.generateToken(id.toString(), List.of(new SimpleGrantedAuthority(Role.ROLE_PLAYER.name())), JwtService.TokenType.PLAYER_ACCESS)).thenReturn("access_token");
        when(jwtService.generateToken(id.toString(), List.of(new SimpleGrantedAuthority(Role.ROLE_PLAYER.name())), JwtService.TokenType.PLAYER_REFRESH)).thenReturn("refresh_token");
        mockLocalDateTimeNow();

        TokenResponse result = securityService.refreshToken("refresh_token", true);
        TokenResponse expected = new TokenResponse("access_token", "refresh_token");

        assertEquals(expected, result);
        assertEquals(LocalDateTime.of(2000, 1, 1, 10, 30), player.getLastSignIn());
    }

    @Test
    void refreshToken_shouldThrowTokenInvalidExceptionWithBadRefreshToken() {
        when(jwtService.isRefreshToken("refresh_token")).thenReturn(false);

        assertThrows(TokenInvalidException.class, () -> securityService.refreshToken("refresh_token", false));
    }

    @Test
    void refreshToken_shouldThrowTokenInvalidExceptionWhenWeHaveIllegalArgumentException() {
        when(jwtService.isRefreshToken("refresh_token")).thenReturn(true);
        when(jwtService.getSubject("refresh_token")).thenThrow(IllegalArgumentException.class);

        assertThrows(TokenInvalidException.class, () -> securityService.refreshToken("refresh_token", false));
    }

    @Test
    void refreshToken_shouldThrowTokenExpiredExceptionWhenWeHaveExpiredJwtException() {
        when(jwtService.isRefreshToken("refresh_token")).thenReturn(true);
        when(jwtService.getSubject("refresh_token")).thenThrow(ExpiredJwtException.class);

        assertThrows(TokenExpiredException.class, () -> securityService.refreshToken("refresh_token", false));
    }

    private void mockLocalDateTimeNow() {
        when(clock.instant()).thenReturn(Instant.parse("2000-01-01T10:30:00Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
    }
}