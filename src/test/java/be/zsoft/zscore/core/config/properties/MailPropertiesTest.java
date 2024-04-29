package be.zsoft.zscore.core.config.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.InvalidPropertyException;

import static org.junit.jupiter.api.Assertions.*;

class MailPropertiesTest {

    @Test
    void shouldNotThrowExceptionWhenOverrideClasspathTemplatesIsDisabled() {
        MailProperties props = new MailProperties(false, null, null, null, null, null, null);

        assertFalse(props.isOverrideClasspathTemplates());
        assertNull(props.getAbsoluteEmailTemplatesPath());
    }

    @Test
    void shouldNotThrowExceptionWhenOverrideClasspathTemplatesIsEnabledAndAbsoluteEmailTemplatesPathGiven() {
        MailProperties props = new MailProperties(true, "/hello", null, null, null, null, null);

        assertTrue(props.isOverrideClasspathTemplates());
        assertEquals("/hello", props.getAbsoluteEmailTemplatesPath());
    }
    @Test
    void shouldThrowExceptionWhenOverrideClasspathTemplatesIsEnabledAndAbsoluteEmailTemplatesPathNotGiven() {
        assertThrows(InvalidPropertyException.class, () -> new MailProperties(true, "", null, null, null, null, null));

    }

}