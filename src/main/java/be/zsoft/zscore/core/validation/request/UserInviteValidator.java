package be.zsoft.zscore.core.validation.request;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.dto.request.user.UserInviteRequest;
import be.zsoft.zscore.core.repository.user.UserInviteRepo;
import be.zsoft.zscore.core.repository.user.UserRepo;
import be.zsoft.zscore.core.service.organization.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserInviteValidator {

    private final UserInviteRepo userInviteRepo;
    private final UserRepo userRepo;
    private final OrganizationService organizationService;

    public void validate(UserInviteRequest request) {
        if (userInviteRepo.doestInviteByEmailAndOrganizationExists(request.email(), organizationService.getMyOrganization())) {
            throw new ApiException(ErrorCodes.ALREADY_INVITED);
        }

        if (userRepo.doesUserByEmailExists(request.email())) {
            throw new ApiException(ErrorCodes.USER_ALREADY_EXISTS);
        }
    }
}
