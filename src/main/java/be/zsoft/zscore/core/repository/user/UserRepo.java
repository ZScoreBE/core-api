package be.zsoft.zscore.core.repository.user;

import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.activationCode = :email")
    Optional<User> findByActivationCode(String email);

    @Query("SELECT u FROM User u WHERE u.passwordResetCode = :email")
    Optional<User> findByPasswordResetCode(String email);

    @Query("SELECT count(u) > 0 FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    boolean doesUserByEmailExists(String email);

    @Query("SELECT u FROM User u WHERE u.organization = :organization ORDER BY u.name ASC")
    Page<User> findAllByOrganization(Organization organization, Pageable pageable);

    @Query("SELECT u FROM User u " +
            "WHERE u.organization = :organization AND (LOWER(u.name) LIKE :search OR LOWER(u.email) LIKE :search) " +
            "ORDER BY u.name ASC")
    Page<User> searchAllOnNameAndEmailByOrganization(String search, Organization organization, Pageable pageable);
}
