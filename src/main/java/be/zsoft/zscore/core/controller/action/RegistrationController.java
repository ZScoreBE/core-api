package be.zsoft.zscore.core.controller.action;

import be.zsoft.zscore.core.dto.mapper.organization.OrganizationMapper;
import be.zsoft.zscore.core.dto.request.action.RegistrationRequest;
import be.zsoft.zscore.core.dto.response.organization.OrganizationResponse;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.user.UserInvite;
import be.zsoft.zscore.core.service.organization.OrganizationService;
import be.zsoft.zscore.core.service.user.UserInviteService;
import be.zsoft.zscore.core.service.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public/registration")
public class RegistrationController {

    private final OrganizationService organizationService;
    private final UserService userService;
    private final UserInviteService userInviteService;
    private final OrganizationMapper organizationMapper;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public OrganizationResponse register(@RequestBody @Valid RegistrationRequest request) {
        UserInvite invite = null;
        Organization organization;

        if (request.inviteCode() != null) {
            invite = userInviteService.getInvite(request.inviteCode());
            organization = invite.getOrganization();
        } else {
            organization = organizationService.createOrganization(request.organization());
        }

        userService.createUser(request.user(), organization, invite == null);

        if ( request.inviteCode() != null) {
            userInviteService.acceptInvite(invite);
        }

        return organizationMapper.toResponse(organization);
    }
}
