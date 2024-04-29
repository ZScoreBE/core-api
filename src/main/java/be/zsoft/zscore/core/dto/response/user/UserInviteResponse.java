package be.zsoft.zscore.core.dto.response.user;

import be.zsoft.zscore.core.entity.user.UserInviteStatus;

import java.util.UUID;

public record UserInviteResponse(
        UUID id,
        String email,
        String name,
        UserInviteStatus status) {
}
