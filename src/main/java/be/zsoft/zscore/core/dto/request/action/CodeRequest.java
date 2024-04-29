package be.zsoft.zscore.core.dto.request.action;

import jakarta.validation.constraints.NotBlank;

public record CodeRequest(
        @NotBlank String code
) {
}
