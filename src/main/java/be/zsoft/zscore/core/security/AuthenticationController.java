package be.zsoft.zscore.core.security;

import be.zsoft.zscore.core.security.dto.request.RefreshTokenRequest;
import be.zsoft.zscore.core.security.dto.request.TokenForPlayerRequest;
import be.zsoft.zscore.core.security.dto.request.TokenRequest;
import be.zsoft.zscore.core.security.dto.response.TokenResponse;
import be.zsoft.zscore.core.security.service.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public/auth/token")
public class AuthenticationController {

    private final SecurityService securityService;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse getToken(@RequestBody @Valid TokenRequest tokenRequest) {
        return securityService.getToken(tokenRequest);
    }

    @PostMapping("/for-player")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse getTokenForPlayer(@RequestBody @Valid TokenForPlayerRequest request) {
        return securityService.getToken(request);
    }

    @PostMapping("/refresh")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse getRefreshToken(@RequestBody @Valid RefreshTokenRequest tokenRequest) {
        return securityService.refreshToken(tokenRequest.refreshToken(), false);
    }

    @PostMapping("/for-player/refresh")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse getRefreshTokenForPlayer(@RequestBody @Valid RefreshTokenRequest tokenRequest) {
        return securityService.refreshToken(tokenRequest.refreshToken(), true);
    }
}
