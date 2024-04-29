package be.zsoft.zscore.core.controller.organization;

import be.zsoft.zscore.core.dto.mapper.organization.OrganizationMapper;
import be.zsoft.zscore.core.dto.request.organization.OrganizationRequest;
import be.zsoft.zscore.core.dto.response.organization.OrganizationResponse;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.service.organization.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    private final OrganizationMapper organizationMapper;
    private final OrganizationService organizationService;

    @Secured({"ROLE_USER"})
    @GetMapping("/myself")
    @ResponseBody
    public OrganizationResponse getMyOrganization() {
        Organization organization = organizationService.getMyOrganization();

        return organizationMapper.toResponse(organization);
    }

    @Secured({"ROLE_USER"})
    @PutMapping("/myself")
    @ResponseBody
    public OrganizationResponse updateOrganization(@RequestBody @Valid OrganizationRequest request) {
        Organization organization = organizationService.updateMyOrganization(request);

        return organizationMapper.toResponse(organization);
    }
}
