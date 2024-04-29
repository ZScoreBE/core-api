package be.zsoft.zscore.core.dto.mapper.organization;

import be.zsoft.zscore.core.dto.request.organization.OrganizationRequest;
import be.zsoft.zscore.core.dto.response.organization.OrganizationResponse;
import be.zsoft.zscore.core.entity.organization.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrganizationMapper {

    public Organization fromRequest(OrganizationRequest request) {
        return fromRequest(request, new Organization());
    }

    public Organization fromRequest(OrganizationRequest request, Organization organization) {
        organization.setName(request.name());
        return organization;
    }

    public OrganizationResponse toResponse(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName()
        );
    }
}
