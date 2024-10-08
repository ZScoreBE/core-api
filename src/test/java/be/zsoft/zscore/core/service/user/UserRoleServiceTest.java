package be.zsoft.zscore.core.service.user;

import be.zsoft.zscore.core.entity.user.Role;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.entity.user.UserRole;
import be.zsoft.zscore.core.fixtures.user.UserFixture;
import be.zsoft.zscore.core.repository.user.UserRoleRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceTest {

    @Mock
    private UserRoleRepo userRoleRepo;

    @InjectMocks
    private UserRoleService userRoleService;

    @Test
    void addRole_shouldAddRole() {
        User user = UserFixture.aDefaultUser();

        userRoleService.addRole(user, Role.ROLE_ORGANIZATION_ADMIN);

        verify(userRoleRepo).saveAndFlush(any(UserRole.class));
    }

    @Test
    void addRole_shouldNotAddRoleIfItExists() {
        User user = UserFixture.aDefaultUser();

        userRoleService.addRole(user, Role.ROLE_USER);

        verify(userRoleRepo, never()).saveAndFlush(any(UserRole.class));
    }

    @Test
    void hasRole_exists() {
        User user = User.builder().roles(List.of(UserRole.builder().role(Role.ROLE_USER).build())).build();

        assertTrue(userRoleService.hasRole(user, Role.ROLE_USER));
    }

    @Test
    void hasRole_doesNotExists() {
        User user = User.builder().roles(List.of()).build();

        assertFalse(userRoleService.hasRole(user, Role.ROLE_USER));
    }

    @Test
    void removeRole_shouldRemove() {
        userRoleService.removeRole(User.builder().build(), Role.ROLE_USER);

        verify(userRoleRepo).deleteByUserAndRole(any(User.class), eq(Role.ROLE_USER));
        verify(userRoleRepo).flush();
    }
}