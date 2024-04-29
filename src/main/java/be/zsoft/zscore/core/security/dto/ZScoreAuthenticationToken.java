package be.zsoft.zscore.core.security.dto;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


@Getter
public class ZScoreAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final AuthenticationData data;
    private final String rawToken;

    public ZScoreAuthenticationToken(AuthenticationData data, String rawToken, Collection<? extends GrantedAuthority> authorities) {
        super(data, data, authorities);

        this.data = data;
        this.rawToken = rawToken;
    }
}
