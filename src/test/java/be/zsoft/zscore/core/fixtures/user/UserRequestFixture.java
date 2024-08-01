package be.zsoft.zscore.core.fixtures.user;

import be.zsoft.zscore.core.dto.request.user.UpdateUserRequest;
import be.zsoft.zscore.core.dto.request.user.UserRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserRequestFixture {

    public static UserRequest aDefaultUserRequest() {
        return new UserRequest("wout@z-soft.be", "wout", "pass");
    }

    public static UpdateUserRequest aDefaultUpdateUserRequest() {
        return new UpdateUserRequest("new name");
    }
}
