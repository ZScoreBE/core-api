package be.zsoft.zscore.core.controller.user;

import be.zsoft.zscore.core.dto.request.action.CodeRequest;
import be.zsoft.zscore.core.dto.request.action.EmailRequest;
import be.zsoft.zscore.core.dto.request.action.PasswordResetRequest;
import be.zsoft.zscore.core.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public/users")
public class PublicUserController {

    private final UserService userService;

    @PostMapping("/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@RequestBody @Valid CodeRequest request) {
        userService.activateUser(request.code());
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forgotPassword(@Valid @RequestBody EmailRequest request) {
        userService.forgotPassword(request.email());
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        userService.resetPassword(request);
    }

    @PostMapping("/send-activation-mail")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendActivationMail(@Valid @RequestBody EmailRequest request) {
        userService.sendActivationMail(request.email());
    }
}
