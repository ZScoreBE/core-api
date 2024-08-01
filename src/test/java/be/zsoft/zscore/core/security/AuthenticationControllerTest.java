package be.zsoft.zscore.core.security;

import be.zsoft.zscore.core.fixtures.token.TokenRequestFixture;
import be.zsoft.zscore.core.fixtures.token.TokenResponseFixture;
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
        TokenRequest request = TokenRequestFixture.aDefaultTokenRequest();
        TokenResponse expected = TokenResponseFixture.aDefaultTokenResponse();

        when(securityService.getToken(request)).thenReturn(expected);

        TokenResponse result = controller.getToken(request);

        assertEquals(expected, result);

        verify(securityService).getToken(request);
    }

    @Test
    void getTokenForPlayer() {
        TokenForPlayerRequest request = TokenRequestFixture.aDefaultTokenForPlayerRequest();
        TokenResponse expected = TokenResponseFixture.aDefaultTokenResponse();

        when(securityService.getToken(request)).thenReturn(expected);

        TokenResponse result = controller.getTokenForPlayer(request);

        assertEquals(expected, result);

        verify(securityService).getToken(request);
    }

    @Test
    void getRefreshToken() {
        RefreshTokenRequest request = TokenRequestFixture.aDefaultRefreshTokenRequest();
        TokenResponse expected = TokenResponseFixture.aDefaultTokenResponse();

        when(securityService.refreshToken("refresh_token", false)).thenReturn(expected);

        TokenResponse result = controller.getRefreshToken(request);

        assertEquals(expected, result);

        verify(securityService).refreshToken("refresh_token", false);
    }

    @Test
    void getRefreshTokenForPlayer() {
        RefreshTokenRequest request = TokenRequestFixture.aDefaultRefreshTokenRequest();
        TokenResponse expected = TokenResponseFixture.aDefaultTokenResponse();

        when(securityService.refreshToken("refresh_token", true)).thenReturn(expected);

        TokenResponse result = controller.getRefreshTokenForPlayer(request);

        assertEquals(expected, result);

        verify(securityService).refreshToken("refresh_token", true);
    }
}