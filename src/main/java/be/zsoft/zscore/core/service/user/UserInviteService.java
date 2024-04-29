package be.zsoft.zscore.core.service.user;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.user.UserInviteMapper;
import be.zsoft.zscore.core.dto.request.user.UserInviteRequest;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.user.UserInvite;
import be.zsoft.zscore.core.entity.user.UserInviteStatus;
import be.zsoft.zscore.core.repository.user.UserInviteRepo;
import be.zsoft.zscore.core.service.organization.OrganizationService;
import be.zsoft.zscore.core.service.utils.SMTPEmailService;
import be.zsoft.zscore.core.validation.request.UserInviteValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInviteService {

    private final UserInviteMapper userInviteMapper;
    private final UserInviteRepo userInviteRepo;
    private final UserInviteValidator userInviteValidator;
    private final OrganizationService organizationService;
    private final SMTPEmailService emailService;

    public UserInvite createInvite(UserInviteRequest request) {
        userInviteValidator.validate(request);

        UserInvite invite = userInviteMapper.fromRequest(request);
        invite.setInviteCode(UUID.randomUUID());
        invite.setStatus(UserInviteStatus.PENDING);
        invite.setOrganization(organizationService.getMyOrganization());

        emailService.sendInviteEmail(invite);

        return userInviteRepo.saveAndFlush(invite);
    }

    public Page<UserInvite> getPendingInvites(Pageable pageable) {
        return userInviteRepo.findAllByStatusAndOrganization(UserInviteStatus.PENDING, organizationService.getMyOrganization(), pageable);
    }

    public Page<UserInvite> searchPendingInvites(String search, Pageable pageable) {
        Organization organization = organizationService.getMyOrganization();
        return userInviteRepo.searchAllOnNameAndEmailByStatusAndOrganization(
                "%" + search.toLowerCase() + "%", UserInviteStatus.PENDING, organization, pageable
        );
    }

    public UserInvite getInvite(UUID inviteCode) {
        return userInviteRepo.findByInviteCode(inviteCode)
                .orElseThrow(() -> new NotFoundException("Could not find any invite with code: %s".formatted(inviteCode)));
    }

    public void acceptInvite(UserInvite invite) {
        invite.setStatus(UserInviteStatus.ACCEPTED);

        userInviteRepo.saveAndFlush(invite);
    }

    public void deleteInvite(UUID id) {
        userInviteRepo.deleteById(id);
    }

    public UserInvite getInviteWithOrganization(UUID inviteCode) {
        return userInviteRepo.findByInviteCodeAndOrganization(inviteCode, organizationService.getMyOrganization())
                .orElseThrow(() -> new NotFoundException("Could not find any invite with code for this organization: %s".formatted(inviteCode)));
    }
}
