package be.zsoft.zscore.core.controller.organization;

import be.zsoft.zscore.core.dto.mapper.organization.OrganizationMapper;
import be.zsoft.zscore.core.dto.request.organization.OrganizationRequest;
import be.zsoft.zscore.core.dto.response.organization.OrganizationResponse;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.service.organization.OrganizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {

    @Mock
    private OrganizationMapper organizationMapper;

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OrganizationController organizationController;

    @Test
    void getMyOrganization() {
        UUID id = UUID.randomUUID();
        Organization organization = Organization.builder().id(id).build();
        OrganizationResponse expected = new OrganizationResponse(id, "name");

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(organizationMapper.toResponse(organization)).thenReturn(expected);

        OrganizationResponse result = organizationController.getMyOrganization();

        verify(organizationService).getMyOrganization();
        verify(organizationMapper).toResponse(organization);

        assertEquals(expected, result);
    }

    @Test
    void updateOrganization() {
        OrganizationRequest request = new OrganizationRequest("newOrg");
        UUID id = UUID.randomUUID();
        Organization organization = Organization.builder().id(id).build();
        OrganizationResponse expected = new OrganizationResponse(id, "name");

        when(organizationService.updateMyOrganization(request)).thenReturn(organization);
        when(organizationMapper.toResponse(organization)).thenReturn(expected);

        OrganizationResponse result = organizationController.updateOrganization(request);

        verify(organizationService).updateMyOrganization(request);
        verify(organizationMapper).toResponse(organization);

        assertEquals(expected, result);
    }

}