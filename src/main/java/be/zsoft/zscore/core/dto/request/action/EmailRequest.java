package be.zsoft.zscore.core.dto.request.action;

import jakarta.validation.constraints.NotBlank;

public record EmailRequest(
        @NotBlank String email
) {
}
