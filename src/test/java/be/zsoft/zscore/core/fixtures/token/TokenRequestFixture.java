package be.zsoft.zscore.core.fixtures.token;

import be.zsoft.zscore.core.security.dto.request.RefreshTokenRequest;
import be.zsoft.zscore.core.security.dto.request.TokenForPlayerRequest;
import be.zsoft.zscore.core.security.dto.request.TokenRequest;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class TokenRequestFixture {

    public static TokenRequest aDefaultTokenRequest() {
        return new TokenRequest("user", "pass");
    }

    public TokenForPlayerRequest aDefaultTokenForPlayerRequest() {
        return new TokenForPlayerRequest(UUID.randomUUID());
    }

    public RefreshTokenRequest aDefaultRefreshTokenRequest() {
        return new RefreshTokenRequest("refresh_token");
    }
}
