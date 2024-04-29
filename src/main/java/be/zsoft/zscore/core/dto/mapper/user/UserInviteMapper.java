package be.zsoft.zscore.core.dto.mapper.user;

import be.zsoft.zscore.core.dto.request.user.UserInviteRequest;
import be.zsoft.zscore.core.dto.response.user.UserInviteResponse;
import be.zsoft.zscore.core.entity.user.UserInvite;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class UserInviteMapper {

    public UserInvite fromRequest(UserInviteRequest request) {
        return UserInvite.builder()
                .name(request.name())
                .email(request.email())
                .build();
    }

    public UserInviteResponse toResponse(UserInvite userInvite) {
        return new UserInviteResponse(
                userInvite.getId(),
                userInvite.getEmail(),
                userInvite.getName(),
                userInvite.getStatus()
        );
    }

    public Page<UserInviteResponse> toResponse(Page<UserInvite> invites) {
        return invites.map(this::toResponse);
    }
}
