package be.zsoft.zscore.core.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record UserInviteRequest(
        @NotBlank String email,
        @NotBlank String name
) {
}
