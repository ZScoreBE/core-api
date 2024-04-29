package be.zsoft.zscore.core.dto.request.user;

import be.zsoft.zscore.core.validation.annotation.PasswordStrength;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank String email,
        @NotBlank String name,
        @NotBlank @PasswordStrength String password
) {
}
