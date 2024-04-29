package be.zsoft.zscore.core.dto.request.organization;

import jakarta.validation.constraints.NotBlank;

public record OrganizationRequest(
        @NotBlank String name
) {
}
