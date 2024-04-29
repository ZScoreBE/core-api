package be.zsoft.zscore.core.security.service;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.security.dto.request.TokenForPlayerRequest;
import be.zsoft.zscore.core.security.dto.request.TokenRequest;
import be.zsoft.zscore.core.security.dto.response.TokenResponse;
import be.zsoft.zscore.core.security.exception.TokenExpiredException;
import be.zsoft.zscore.core.security.exception.TokenInvalidException;
import be.zsoft.zscore.core.service.player.PlayerService;
import be.zsoft.zscore.core.service.user.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class SecurityService {

    private final UserService userService;
    private final PlayerService playerService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Clock clock;

    public TokenResponse getToken(TokenRequest tokenRequest) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(tokenRequest.username(), tokenRequest.password());
        // Only used to validate the username/password combo
        Authentication authentication = authenticationManager.authenticate(authToken);
        User user = userService.loadUserByUsername(authentication.getName());

        if (!user.isActivated()) {
            throw new ApiException(ErrorCodes.NOT_ACTIVATED);
        }

        return createUserTokens(user);
    }

    public TokenResponse getToken(TokenForPlayerRequest request) {
        Player player = playerService.getPlayerById(request.id());
        updatePlayerLastSignIn(player);
        return createPlayerTokens(player);
    }

    public TokenResponse refreshToken(String refreshToken, boolean forPlayer) {
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new TokenInvalidException("Not a refresh token");
        }

        try {
            UUID id = UUID.fromString(jwtService.getSubject(refreshToken));

            if (forPlayer) {
                Player player = playerService.getPlayerById(id);
                updatePlayerLastSignIn(player);
                return createPlayerTokens(player);
            } else {
                User user = userService.getById(id);
                return createUserTokens(user);
            }
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Refresh JWT Token has expired");
        } catch (IllegalArgumentException e) {
            throw new TokenInvalidException("Refresh Invalid JWT Token");
        }
    }

    private void updatePlayerLastSignIn(Player player) {
        player.setLastSignIn(LocalDateTime.now(clock));
        playerService.rawUpdate(player);
    }


    private TokenResponse createUserTokens(User user) {
        String accessToken = jwtService.generateToken(user, JwtService.TokenType.ACCESS);
        String refreshToken = jwtService.generateToken(user, JwtService.TokenType.REFRESH);

        return new TokenResponse(accessToken, refreshToken);
    }

    private TokenResponse createPlayerTokens(Player player) {
        String accessToken = jwtService.generateToken(player.getId().toString(), player.getAuthorities(), JwtService.TokenType.PLAYER_ACCESS);
        String refreshToken = jwtService.generateToken(player.getId().toString(), player.getAuthorities(), JwtService.TokenType.PLAYER_REFRESH);

        return new TokenResponse(accessToken, refreshToken);
    }

}
