package be.zsoft.zscore.core.config.properties;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "zscore.email")
public class MailProperties {

    private final boolean overrideClasspathTemplates;
    private final String absoluteEmailTemplatesPath;
    private final String noReplyEmail;
    private final String noReplyName;
    private final String activationFrontendUrl;
    private final String resetPasswordFrontendUrl;
    private final String registerFrontendUrl;

    @ConstructorBinding
    public MailProperties(boolean overrideClasspathTemplates, String absoluteEmailTemplatesPath, String noReplyEmail, String noReplyName, String activationFrontendUrl, String resetPasswordFrontendUrl, String registerFrontendUrl) {
        this.overrideClasspathTemplates = overrideClasspathTemplates;
        this.absoluteEmailTemplatesPath = absoluteEmailTemplatesPath;
        this.noReplyEmail = noReplyEmail;
        this.noReplyName = noReplyName;
        this.activationFrontendUrl = activationFrontendUrl;
        this.resetPasswordFrontendUrl = resetPasswordFrontendUrl;
        this.registerFrontendUrl = registerFrontendUrl;

        validate();
    }

    private void validate() {
        if (overrideClasspathTemplates && StringUtils.isBlank(absoluteEmailTemplatesPath)) {
            throw new InvalidPropertyException(
                    MailProperties.class,
                    "absoluteEmailTemplatesPath",
                    "When 'zscore.email.overrideClasspathTemplates' is set to true you need to define the following property: 'zscore.email.absoluteEmailTemplatesPath'"
            );
        }
    }
}
