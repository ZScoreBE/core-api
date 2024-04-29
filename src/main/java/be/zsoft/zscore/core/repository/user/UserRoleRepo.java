package be.zsoft.zscore.core.repository.user;

import be.zsoft.zscore.core.entity.user.Role;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, UUID> {

    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.user = :user AND ur.role = :role")
    void deleteByUserAndRole(User user, Role role);
}
