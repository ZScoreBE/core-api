package be.zsoft.zscore.core.dto.request.action;

import be.zsoft.zscore.core.validation.annotation.PasswordStrength;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequest(
        @NotBlank String code,
        @NotBlank @PasswordStrength String password
) {
}
