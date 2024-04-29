package be.zsoft.zscore.core.validation;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.dto.request.user.UserRequest;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.repository.user.UserRepo;
import be.zsoft.zscore.core.validation.request.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    void validate_success() {
        when(userRepo.findByEmail("wout@z-soft.be")).thenReturn(Optional.empty());

        userValidator.validate(new UserRequest("wout@z-soft.be", "wout", "pass"));

        verify(userRepo).findByEmail("wout@z-soft.be");
    }

    @Test
    void validate_notUnique() {
        when(userRepo.findByEmail("wout@z-soft.be")).thenReturn(Optional.of(User.builder().build()));

        ApiException ex = assertThrows(
                ApiException.class,
                () -> userValidator.validate(new UserRequest("wout@z-soft.be", "wout", "pass"))
        );

        assertEquals(ErrorCodes.USER_EMAIL_NOT_UNIQUE, ex.getErrorKey());
        verify(userRepo).findByEmail("wout@z-soft.be");
    }
}