package be.zsoft.zscore.core.service.organization;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.dto.mapper.organization.OrganizationMapper;
import be.zsoft.zscore.core.dto.request.organization.OrganizationRequest;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.repository.organization.OrganizationRepo;
import be.zsoft.zscore.core.security.dto.AuthenticationData;
import be.zsoft.zscore.core.security.dto.ZScoreAuthenticationToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationMapper organizationMapper;

    @Mock
    private OrganizationRepo organizationRepo;

    @InjectMocks
    private OrganizationService organizationService;

    @Test
    void createOrganization() {
        OrganizationRequest request = new OrganizationRequest("org");
        Organization organization = Organization.builder().id(UUID.randomUUID()).build();

        when(organizationMapper.fromRequest(request)).thenReturn(organization);
        when(organizationRepo.saveAndFlush(organization)).thenReturn(organization);

        Organization result = organizationService.createOrganization(new OrganizationRequest("org"));

        verify(organizationMapper).fromRequest(request);
        verify(organizationRepo).saveAndFlush(organization);

        assertEquals(organization, result);
    }

    @Test
    void createOrganization_requestNull() {
        ApiException ex = assertThrows(ApiException.class, () -> organizationService.createOrganization(null));

        assertEquals(ErrorCodes.ORGANIZATION_NAME_IS_REQUIRED, ex.getErrorKey());
    }

    @Test
    void getMyOrganization_success() {
        Organization expected = Organization.builder().id(UUID.randomUUID()).build();
        User user = User.builder().id(UUID.randomUUID()).organization(expected).build();

        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(user, null, null, expected), "token", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertEquals(expected, organizationService.getMyOrganization());

        SecurityContextHolder.clearContext();
    }

    @Test
    void getMyOrganization_notLoggedIn() {
        SecurityContextHolder.clearContext();
        ApiException ex = assertThrows(ApiException.class, () -> organizationService.getMyOrganization());

        assertEquals(ErrorCodes.ACCESS_DENIED, ex.getErrorKey());
    }

    @Test
    void updateMyOrganization() {
        OrganizationRequest request = new OrganizationRequest("newOrg");
        Organization organization = Organization.builder().id(UUID.randomUUID()).build();
        User user = User.builder().id(UUID.randomUUID()).organization(organization).build();

        Authentication auth = new ZScoreAuthenticationToken(new AuthenticationData(user, null, null, organization), "token", null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(organizationMapper.fromRequest(request, organization)).thenReturn(organization);
        when(organizationRepo.saveAndFlush(organization)).thenReturn(organization);

        organizationService.updateMyOrganization(request);

        verify(organizationMapper).fromRequest(request, organization);
        verify(organizationRepo).saveAndFlush(organization);

        SecurityContextHolder.clearContext();
    }
}