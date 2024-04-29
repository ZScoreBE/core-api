package be.zsoft.zscore.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = {
		"be.zsoft.zscore.core.config.properties",
		"be.zsoft.zscore.core.security.properties",
})
@EnableJpaRepositories(basePackages = {"be.zsoft.zscore.core.repository"})
@SpringBootApplication
public class CoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreApiApplication.class, args);
	}

}
