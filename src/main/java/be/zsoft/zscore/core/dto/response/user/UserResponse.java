package be.zsoft.zscore.core.dto.response.user;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        boolean activated,
        boolean organizationAdmin
) {
}
