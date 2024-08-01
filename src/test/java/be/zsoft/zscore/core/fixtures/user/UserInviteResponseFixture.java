package be.zsoft.zscore.core.fixtures.user;

import be.zsoft.zscore.core.dto.response.user.UserInviteResponse;
import be.zsoft.zscore.core.entity.user.UserInviteStatus;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UserInviteResponseFixture {

    public static UserInviteResponse aDefaultUserInviteResponse() {
        return new UserInviteResponse(
                UUID.randomUUID(),
                "wout@z-soft.be",
                "wout",
                UserInviteStatus.PENDING
        );
    }
}
