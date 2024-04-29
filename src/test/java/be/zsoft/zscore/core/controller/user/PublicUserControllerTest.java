package be.zsoft.zscore.core.controller.user;

import be.zsoft.zscore.core.dto.request.action.CodeRequest;
import be.zsoft.zscore.core.dto.request.action.EmailRequest;
import be.zsoft.zscore.core.dto.request.action.PasswordResetRequest;
import be.zsoft.zscore.core.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PublicUserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private PublicUserController controller;

    @Test
    void activate() {
        controller.activate(new CodeRequest("code"));
        verify(userService).activateUser("code");
    }

    @Test
    void forgotPassword() {
        controller.forgotPassword(new EmailRequest("wout@z-soft.be"));
        verify(userService).forgotPassword("wout@z-soft.be");
    }

    @Test
    void resetPassword() {
        PasswordResetRequest request = new PasswordResetRequest("code", "pass");
        controller.resetPassword(request);
        verify(userService).resetPassword(request);
    }

    @Test
    void sendActivationMail() {
        controller.sendActivationMail(new EmailRequest("wout@z-soft.be"));
        verify(userService).sendActivationMail("wout@z-soft.be");
    }
}