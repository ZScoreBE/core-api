package be.zsoft.zscore.core.dto.response.organization;

import java.util.UUID;

public record OrganizationResponse(
        UUID id,
        String name
) {
}
