package be.zsoft.zscore.core.security;

import be.zsoft.zscore.core.security.dto.request.RefreshTokenRequest;
import be.zsoft.zscore.core.security.dto.request.TokenForPlayerRequest;
import be.zsoft.zscore.core.security.dto.request.TokenRequest;
import be.zsoft.zscore.core.security.dto.response.TokenResponse;
import be.zsoft.zscore.core.security.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private AuthenticationController controller;

    @Test
    void getToken() {
        TokenRequest request = new TokenRequest("user", "pass");
        TokenResponse expected = new TokenResponse("access_token", "refresh_token");

        when(securityService.getToken(request)).thenReturn(expected);

        TokenResponse result = controller.getToken(request);

        assertEquals(expected, result);

        verify(securityService).getToken(request);
    }

    @Test
    void getTokenForPlayer() {
        TokenForPlayerRequest request = new TokenForPlayerRequest(UUID.randomUUID());
        TokenResponse expected = new TokenResponse("access_token", "refresh_token");

        when(securityService.getToken(request)).thenReturn(expected);

        TokenResponse result = controller.getTokenForPlayer(request);

        assertEquals(expected, result);

        verify(securityService).getToken(request);
    }

    @Test
    void getRefreshToken() {
        RefreshTokenRequest request = new RefreshTokenRequest("refresh_token");
        TokenResponse expected = new TokenResponse("access_token", "refresh_token");

        when(securityService.refreshToken("refresh_token", false)).thenReturn(expected);

        TokenResponse result = controller.getRefreshToken(request);

        assertEquals(expected, result);

        verify(securityService).refreshToken("refresh_token", false);
    }

    @Test
    void getRefreshTokenForPlayer() {
        RefreshTokenRequest request = new RefreshTokenRequest("refresh_token");
        TokenResponse expected = new TokenResponse("access_token", "refresh_token");

        when(securityService.refreshToken("refresh_token", true)).thenReturn(expected);

        TokenResponse result = controller.getRefreshTokenForPlayer(request);

        assertEquals(expected, result);

        verify(securityService).refreshToken("refresh_token", true);
    }
}