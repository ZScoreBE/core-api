package be.zsoft.zscore.core.fixtures.token;

import be.zsoft.zscore.core.security.dto.response.TokenResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TokenResponseFixture {

    public static TokenResponse aDefaultTokenResponse() {
        return new TokenResponse("access_token", "refresh_token");
    }
}
