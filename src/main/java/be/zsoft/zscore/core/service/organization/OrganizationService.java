package be.zsoft.zscore.core.service.organization;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.dto.mapper.organization.OrganizationMapper;
import be.zsoft.zscore.core.dto.request.organization.OrganizationRequest;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.repository.organization.OrganizationRepo;
import be.zsoft.zscore.core.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrganizationService {

    private final OrganizationMapper organizationMapper;
    private final OrganizationRepo organizationRepo;

    public Organization createOrganization(OrganizationRequest request) {
        if (request == null) {
            throw new ApiException(ErrorCodes.ORGANIZATION_NAME_IS_REQUIRED);
        }

        Organization organization = organizationMapper.fromRequest(request);
        return organizationRepo.saveAndFlush(organization);
    }

    public Organization getMyOrganization() {
        return SecurityUtils.getAuthenticatedOrganization()
                .orElseThrow(() -> new ApiException(ErrorCodes.ACCESS_DENIED));
    }

    public Organization updateMyOrganization(OrganizationRequest request) {
        Organization organization = organizationMapper.fromRequest(request, getMyOrganization());
        return organizationRepo.saveAndFlush(organization);
    }
}
