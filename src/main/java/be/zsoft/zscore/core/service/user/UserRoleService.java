package be.zsoft.zscore.core.service.user;

import be.zsoft.zscore.core.entity.user.Role;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.entity.user.UserRole;
import be.zsoft.zscore.core.repository.user.UserRoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserRoleService {

    private final UserRoleRepo userRoleRepo;

    public void addRole(User user, Role role) {
        log.info("Adding role '{}' to user '{}'", role.name(), user.getId());
        if (hasRole(user, role)) {
            return;
        }

        var userRole = UserRole.builder()
                .user(user)
                .role(role)
                .build();

        userRoleRepo.saveAndFlush(userRole);
    }

    public boolean hasRole(User user, Role role) {
        if (user.getRoles() == null) {
            return false;
        }

        return user.getRoles().stream()
                .map(UserRole::getRole)
                .anyMatch(r -> r == role);
    }

    public void removeRole(User user, Role role) {
        log.info("Removing role '{}' from user '{}'", role.name(), user.getId());
        userRoleRepo.deleteByUserAndRole(user, role);
        userRoleRepo.flush();
    }
}
