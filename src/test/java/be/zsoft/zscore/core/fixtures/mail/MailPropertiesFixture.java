package be.zsoft.zscore.core.fixtures.mail;

import be.zsoft.zscore.core.config.properties.MailProperties;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MailPropertiesFixture {

    public static MailProperties aMailPropertiesWithoutOverrideClassPathTemplate() {
        return new MailProperties(
                false,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public static MailProperties aMailPropertiesWithOverrideClassPathTemplate() {
        return new MailProperties(
                true,
                "/hello",
                null,
                null,
                null,
                null,
                null
        );
    }

    public static MailProperties aMailPropertiesWithOverrideClassPathTemplateAndEmptyPath() {
        return new MailProperties(
                true,
                "",
                null,
                null,
                null,
                null,
                null
        );
    }

}
