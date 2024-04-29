package be.zsoft.zscore.core.repository.user;

import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.user.UserInvite;
import be.zsoft.zscore.core.entity.user.UserInviteStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserInviteRepo extends JpaRepository<UserInvite, UUID> {

    @Query("SELECT ui FROM UserInvite ui WHERE ui.inviteCode = :code AND ui.organization = :organization")
    Optional<UserInvite> findByInviteCodeAndOrganization(UUID code, Organization organization);

    @Query("SELECT ui FROM UserInvite ui WHERE ui.status = :status AND ui.organization = :organization ORDER BY ui.name ASC")
    Page<UserInvite> findAllByStatusAndOrganization(UserInviteStatus status, Organization organization, Pageable pageable);

    @Query("SELECT ui FROM UserInvite ui " +
            "WHERE ui.status = :status AND ui.organization = :organization AND (LOWER(ui.name) LIKE :search OR LOWER(ui.email) LIKE :search) " +
            "ORDER BY ui.name ASC")
    Page<UserInvite> searchAllOnNameAndEmailByStatusAndOrganization(
            String search, UserInviteStatus status, Organization organization, Pageable pageable
    );

    @Query("SELECT count(ui) > 0 FROM UserInvite ui WHERE ui.email = :email AND ui.organization = :organization")
    boolean doestInviteByEmailAndOrganizationExists(String email, Organization organization);

    @Query("SELECT ui FROM UserInvite ui WHERE ui.inviteCode = :code")
    Optional<UserInvite> findByInviteCode(UUID code);
}
