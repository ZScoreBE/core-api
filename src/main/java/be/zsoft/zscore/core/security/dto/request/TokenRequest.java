package be.zsoft.zscore.core.security.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
