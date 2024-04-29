package be.zsoft.zscore.core.security;

import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.security.dto.AuthenticationData;
import be.zsoft.zscore.core.security.dto.ZScoreAuthenticationToken;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@UtilityClass
public class SecurityUtils {

    public static void checkIfOwnedByUser(User owner, String errorKey) {
        User user = getAuthenticatedUser()
                .orElseThrow(() -> new ApiException(errorKey));

        if (!owner.getId().equals(user.getId())) {
            throw new ApiException(errorKey);
        }
    }

    public static void checkIfOwnedByOrganization(Organization owner, String errorKey) throws ApiException {
        Organization organization = getAuthenticatedOrganization()
                .orElseThrow(() -> new ApiException(errorKey));

        if (!owner.getId().equals(organization.getId())) {
            throw new ApiException(errorKey);
        }
    }

    public static boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth instanceof ZScoreAuthenticationToken;
    }

    public static Optional<User> getAuthenticatedUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken))
                .flatMap(authentication -> Optional.ofNullable(((ZScoreAuthenticationToken) authentication).getData())
                        .map(AuthenticationData::user)
                );
    }

    public static Optional<Organization> getAuthenticatedOrganization() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken))
                .flatMap(authentication -> Optional.ofNullable(((ZScoreAuthenticationToken) authentication).getData())
                        .map(AuthenticationData::organization)
                );
    }

    public static Optional<Player> getAuthenticatedPlayer() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(authentication -> authentication instanceof ZScoreAuthenticationToken)
                .flatMap(authentication -> Optional.ofNullable(((ZScoreAuthenticationToken) authentication).getData())
                        .map(AuthenticationData::player)
                );
    }

    public static Optional<Game> getAuthenticatedGame() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(authentication -> authentication instanceof ZScoreAuthenticationToken)
                .flatMap(authentication -> Optional.ofNullable(((ZScoreAuthenticationToken) authentication).getData())
                        .map(AuthenticationData::game)
                );
    }
}
