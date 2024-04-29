package be.zsoft.zscore.core.controller.action;

import be.zsoft.zscore.core.dto.mapper.organization.OrganizationMapper;
import be.zsoft.zscore.core.dto.request.action.RegistrationRequest;
import be.zsoft.zscore.core.dto.request.organization.OrganizationRequest;
import be.zsoft.zscore.core.dto.request.user.UserRequest;
import be.zsoft.zscore.core.dto.response.organization.OrganizationResponse;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.user.UserInvite;
import be.zsoft.zscore.core.service.organization.OrganizationService;
import be.zsoft.zscore.core.service.user.UserInviteService;
import be.zsoft.zscore.core.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    @Mock
    private OrganizationService organizationService;

    @Mock
    private UserService userService;

    @Mock
    private OrganizationMapper organizationMapper;

    @Mock
    private UserInviteService userInviteService;

    @InjectMocks
    private RegistrationController controller;

    @Test
    void register_withOrganization() {
        OrganizationRequest organizationRequest = new OrganizationRequest("org");
        UserRequest userRequest = new UserRequest("wout@z-soft.be", "wout", "pass");
        RegistrationRequest request = new RegistrationRequest(organizationRequest, userRequest, null);

        Organization organization = Organization.builder().id(UUID.randomUUID()).build();
        OrganizationResponse expected = new OrganizationResponse(UUID.randomUUID(), "org");

        when(organizationService.createOrganization(organizationRequest)).thenReturn(organization);
        when(organizationMapper.toResponse(organization)).thenReturn(expected);

        OrganizationResponse result = controller.register(request);

        assertEquals(expected, result);

        verify(userInviteService, never()).getInvite(any(UUID.class));
        verify(organizationService).createOrganization(organizationRequest);
        verify(userService).createUser(userRequest, organization, true);
        verify(organizationMapper).toResponse(organization);
    }

    @Test
    void register_withInvite() {
        UUID inviteCode = UUID.randomUUID();

        UserRequest userRequest = new UserRequest("wout@z-soft.be", "wout", "pass");
        RegistrationRequest request = new RegistrationRequest(null, userRequest, inviteCode);

        Organization organization = Organization.builder().id(UUID.randomUUID()).build();
        UserInvite invite = UserInvite.builder().id(UUID.randomUUID()).organization(organization).build();
        OrganizationResponse expected = new OrganizationResponse(UUID.randomUUID(), "org");

        when(userInviteService.getInvite(inviteCode)).thenReturn(invite);
        when(organizationMapper.toResponse(organization)).thenReturn(expected);

        OrganizationResponse result = controller.register(request);

        assertEquals(expected, result);

        verify(userInviteService).getInvite(inviteCode);
        verify(organizationService, never()).createOrganization(any(OrganizationRequest.class));
        verify(userService).createUser(userRequest, organization, false);
        verify(userInviteService).acceptInvite(invite);
        verify(organizationMapper).toResponse(organization);
    }
}