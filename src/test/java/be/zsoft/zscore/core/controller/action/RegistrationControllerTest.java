package be.zsoft.zscore.core.controller.action;

import be.zsoft.zscore.core.dto.mapper.organization.OrganizationMapper;
import be.zsoft.zscore.core.dto.request.action.RegistrationRequest;
import be.zsoft.zscore.core.dto.request.organization.OrganizationRequest;
import be.zsoft.zscore.core.dto.request.user.UserRequest;
import be.zsoft.zscore.core.dto.response.organization.OrganizationResponse;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.user.UserInvite;
import be.zsoft.zscore.core.fixtures.organization.OrganizationFixture;
import be.zsoft.zscore.core.fixtures.organization.OrganizationRequestFixture;
import be.zsoft.zscore.core.fixtures.organization.OrganizationResponseFixture;
import be.zsoft.zscore.core.fixtures.user.UserInviteFixture;
import be.zsoft.zscore.core.fixtures.user.UserRequestFixture;
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
        OrganizationRequest organizationRequest = OrganizationRequestFixture.aDefaultOrganizationRequest();
        UserRequest userRequest = UserRequestFixture.aDefaultUserRequest();
        RegistrationRequest request = new RegistrationRequest(organizationRequest, userRequest, null);

        Organization organization = OrganizationFixture.aDefaultOrganization();
        OrganizationResponse expected = OrganizationResponseFixture.aDefaultOrganizationResponse();

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

        UserRequest userRequest = UserRequestFixture.aDefaultUserRequest();
        RegistrationRequest request = new RegistrationRequest(null, userRequest, inviteCode);

        UserInvite invite = UserInviteFixture.aDefaultInvite();
        Organization organization = invite.getOrganization();
        OrganizationResponse expected = OrganizationResponseFixture.aDefaultOrganizationResponse();

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