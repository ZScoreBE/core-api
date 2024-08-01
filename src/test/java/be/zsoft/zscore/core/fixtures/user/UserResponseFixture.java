package be.zsoft.zscore.core.fixtures.user;

import be.zsoft.zscore.core.dto.response.user.UserResponse;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UserResponseFixture {

    public static UserResponse aDefaultUserResponse() {
        return new UserResponse(
                UUID.randomUUID(),
                "name",
                "wout@z-soft.be",
                true,
                false
        );
    }
}
