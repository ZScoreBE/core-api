package be.zsoft.zscore.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class CommonConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
