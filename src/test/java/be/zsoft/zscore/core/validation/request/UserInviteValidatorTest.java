package be.zsoft.zscore.core.validation.request;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.dto.request.user.UserInviteRequest;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.repository.user.UserInviteRepo;
import be.zsoft.zscore.core.repository.user.UserRepo;
import be.zsoft.zscore.core.service.organization.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInviteValidatorTest {

    private static final UserInviteRequest REQUEST = new UserInviteRequest("wout@z-soft.be", "wout");
    private static final Organization ORGANIZATION = Organization.builder().id(UUID.randomUUID()).build();

    @Mock
    private UserInviteRepo userInviteRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private UserInviteValidator userInviteValidator;

    @BeforeEach
    void setup() {
        when(organizationService.getMyOrganization()).thenReturn(ORGANIZATION);
    }

    @Test
    void validate_alreadyInvited() {
        when(userInviteRepo.doestInviteByEmailAndOrganizationExists("wout@z-soft.be", ORGANIZATION)).thenReturn(true);

        ApiException ex = assertThrows(ApiException.class, () -> userInviteValidator.validate(REQUEST));

        assertEquals(ErrorCodes.ALREADY_INVITED, ex.getErrorKey());

        verify(userInviteRepo).doestInviteByEmailAndOrganizationExists("wout@z-soft.be", ORGANIZATION);
        verify(userRepo, never()).doesUserByEmailExists("wout@z-soft.be");
    }

    @Test
    void validate_userAlreadyExists() {
        when(userInviteRepo.doestInviteByEmailAndOrganizationExists("wout@z-soft.be", ORGANIZATION)).thenReturn(false);
        when(userRepo.doesUserByEmailExists("wout@z-soft.be")).thenReturn(true);

        ApiException ex = assertThrows(ApiException.class, () -> userInviteValidator.validate(REQUEST));

        assertEquals(ErrorCodes.USER_ALREADY_EXISTS, ex.getErrorKey());

        verify(userInviteRepo).doestInviteByEmailAndOrganizationExists("wout@z-soft.be", ORGANIZATION);
        verify(userRepo).doesUserByEmailExists("wout@z-soft.be");
    }

    @Test
    void validate_success() {
        when(userInviteRepo.doestInviteByEmailAndOrganizationExists("wout@z-soft.be", ORGANIZATION)).thenReturn(false);
        when(userRepo.doesUserByEmailExists("wout@z-soft.be")).thenReturn(false);

        userInviteValidator.validate(REQUEST);

        verify(userInviteRepo).doestInviteByEmailAndOrganizationExists("wout@z-soft.be", ORGANIZATION);
        verify(userRepo).doesUserByEmailExists("wout@z-soft.be");

    }
}