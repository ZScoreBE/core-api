package be.zsoft.zscore.core.dto.request.user;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank String name
) {
}
