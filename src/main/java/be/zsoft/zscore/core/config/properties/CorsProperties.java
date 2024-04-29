package be.zsoft.zscore.core.config.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.List;

@Getter
@ConfigurationProperties(prefix = "zscore.cors")
public class CorsProperties {

    private final List<String> allowedOrigins;

    @ConstructorBinding
    public CorsProperties(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}
