package be.zsoft.zscore.core.security;

import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.fixtures.game.GameFixture;
import be.zsoft.zscore.core.fixtures.player.PlayerFixture;
import be.zsoft.zscore.core.fixtures.user.UserFixture;
import be.zsoft.zscore.core.security.dto.AuthenticationData;
import be.zsoft.zscore.core.security.dto.ZScoreAuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SecurityUtilsTest {

    @BeforeEach
    public void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void checkIfOwnedByUser_shouldThrowApiExceptionIfNotOwned() {
        User user = UserFixture.aDefaultUser();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(user, null, null, user.getOrganization()), "token", null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(ApiException.class, () -> SecurityUtils.checkIfOwnedByUser(User.builder().id(UUID.randomUUID()).build(), ""));
    }

    @Test
    void checkIfOwnedByUser_shouldThrowApiExceptionIfNoAuthenticatedUserIsFound() {
        assertThrows(ApiException.class, () -> SecurityUtils.checkIfOwnedByUser(User.builder().id(UUID.randomUUID()).build(), ""));
    }

    @Test
    void checkIfOwnedByOrganization_shouldThrowApiExceptionIfNotOwned() {
        User user = UserFixture.aDefaultUser();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(user, null, null, user.getOrganization()), "token", null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(ApiException.class, () -> SecurityUtils.checkIfOwnedByOrganization(Organization.builder().id(UUID.randomUUID()).build(), ""));
    }

    @Test
    void checkIfOwnedByOrganization_shouldThrowApiExceptionIfNoAuthenticatedOrganizationIsFound() {
        assertThrows(ApiException.class, () -> SecurityUtils.checkIfOwnedByOrganization(Organization.builder().id(UUID.randomUUID()).build(), ""));
    }

    @Test
    void getAuthenticatedUser_shouldReturnUser() {
        User user = UserFixture.aDefaultUser();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(user, null, null, null), "token", null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertEquals(user, SecurityUtils.getAuthenticatedUser().get());
    }

    @Test
    void getAuthenticatedUser_shouldReturnEmptyOptionalIfNoValidAuthenticationIsFound() {
        SecurityContextHolder.getContext().setAuthentication(
                new AnonymousAuthenticationToken("abc", "abc", List.of(new SimpleGrantedAuthority("ROLE_USER")))
        );
        assertTrue(SecurityUtils.getAuthenticatedUser().isEmpty());
    }

    @Test
    void isAuthenticated_shouldReturnTrueIfAuthenticated() {
        User user = UserFixture.aDefaultUser();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(user, null, null, null), "token", null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertTrue(SecurityUtils.isAuthenticated());
    }

    @Test
    void isAuthenticated_shouldReturnFalseIfNoAuthenticationFound() {
        assertFalse(SecurityUtils.isAuthenticated());
    }

    @Test
    void getAuthenticatedOrganization_shouldReturnOrganization() {
        User user = UserFixture.aDefaultUser();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(user, null, null, user.getOrganization()), "token", null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertEquals(user.getOrganization(), SecurityUtils.getAuthenticatedOrganization().get());
    }

    @Test
    void getAuthenticatedOrganization_shouldReturnEmptyOptionalIfNoValidAuthenticationIsFound() {
        SecurityContextHolder.getContext().setAuthentication(
                new AnonymousAuthenticationToken("abc", "abc", List.of(new SimpleGrantedAuthority("ROLE_USER")))
        );
        assertTrue(SecurityUtils.getAuthenticatedOrganization().isEmpty());
    }

    @Test
    void getAuthenticatedPlayer_shouldReturnPlayer() {
        Player player = PlayerFixture.aDefaultPlayer();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, player, null, null), "token", null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertEquals(player, SecurityUtils.getAuthenticatedPlayer().get());
    }

    @Test
    void getAuthenticatedPlayer_shouldReturnEmptyOptionalIfNoValidAuthenticationIsFound() {
        SecurityContextHolder.getContext().setAuthentication(
                new AnonymousAuthenticationToken("abc", "abc", List.of(new SimpleGrantedAuthority("ROLE_USER")))
        );
        assertTrue(SecurityUtils.getAuthenticatedPlayer().isEmpty());
    }

    @Test
    void getAuthenticatedGame_shouldReturnGame() {
        Game game = GameFixture.aDefaultGame();
        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(null, null, game, null), "token", null);

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertEquals(game, SecurityUtils.getAuthenticatedGame().get());
    }

    @Test
    void getAuthenticatedGame_shouldReturnEmptyOptionalIfNoValidAuthenticationIsFound() {
        SecurityContextHolder.getContext().setAuthentication(
                new AnonymousAuthenticationToken("abc", "abc", List.of(new SimpleGrantedAuthority("ROLE_USER")))
        );
        assertTrue(SecurityUtils.getAuthenticatedGame().isEmpty());
    }
}