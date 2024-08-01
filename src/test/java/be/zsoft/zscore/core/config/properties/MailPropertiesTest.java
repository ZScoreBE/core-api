package be.zsoft.zscore.core.config.properties;

import be.zsoft.zscore.core.fixtures.mail.MailPropertiesFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.InvalidPropertyException;

import static org.junit.jupiter.api.Assertions.*;

class MailPropertiesTest {

    @Test
    void shouldNotThrowExceptionWhenOverrideClasspathTemplatesIsDisabled() {
        MailProperties props = MailPropertiesFixture.aMailPropertiesWithoutOverrideClassPathTemplate();

        assertFalse(props.isOverrideClasspathTemplates());
        assertNull(props.getAbsoluteEmailTemplatesPath());
    }

    @Test
    void shouldNotThrowExceptionWhenOverrideClasspathTemplatesIsEnabledAndAbsoluteEmailTemplatesPathGiven() {
        MailProperties props = MailPropertiesFixture.aMailPropertiesWithOverrideClassPathTemplate();

        assertTrue(props.isOverrideClasspathTemplates());
        assertEquals("/hello", props.getAbsoluteEmailTemplatesPath());
    }
    @Test
    void shouldThrowExceptionWhenOverrideClasspathTemplatesIsEnabledAndAbsoluteEmailTemplatesPathNotGiven() {
        assertThrows(
                InvalidPropertyException.class,
                MailPropertiesFixture::aMailPropertiesWithOverrideClassPathTemplateAndEmptyPath
        );

    }

}