package be.zsoft.zscore.core.fixtures.user;

import be.zsoft.zscore.core.dto.request.user.UserInviteRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserInviteRequestFixture {

    public static UserInviteRequest aDefaultUserInviteRequest() {
        return new UserInviteRequest(
                "wout@z-soft.be",
                "wout"
        );
    }
}
