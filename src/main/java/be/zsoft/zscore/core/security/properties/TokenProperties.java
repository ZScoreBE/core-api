package be.zsoft.zscore.core.security.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "zscore.security.token")
public class TokenProperties {

    private final String secret;
    private final int tokenExpiryDuration;
    private final int refreshTokenExpiryDuration;
    private final int playerTokenExpiryDuration;
    private final int playerRefreshTokenExpiryDuration;

    @ConstructorBinding
    public TokenProperties(String secret, int tokenExpiryDuration, int refreshTokenExpiryDuration, int playerTokenExpiryDuration, int playerRefreshTokenExpiryDuration) {
        this.secret = secret;
        this.tokenExpiryDuration = tokenExpiryDuration;
        this.refreshTokenExpiryDuration = refreshTokenExpiryDuration;
        this.playerTokenExpiryDuration = playerTokenExpiryDuration;
        this.playerRefreshTokenExpiryDuration = playerRefreshTokenExpiryDuration;
    }
}
