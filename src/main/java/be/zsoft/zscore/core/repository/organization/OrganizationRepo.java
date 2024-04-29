package be.zsoft.zscore.core.repository.organization;

import be.zsoft.zscore.core.entity.organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationRepo extends JpaRepository<Organization, UUID> {

}
