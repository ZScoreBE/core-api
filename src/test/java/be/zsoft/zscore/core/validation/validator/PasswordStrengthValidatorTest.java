package be.zsoft.zscore.core.validation.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PasswordStrengthValidatorTest {

    @InjectMocks
    private PasswordStrengthValidator validator;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(validator, "minScore", 2);
    }

    @Test
    void isValid_badPassword() {
        assertFalse(validator.isValid("testtest", null));
    }

    @Test
    void isValid_goodPassword() {
        assertTrue(validator.isValid("sdrfsIOUHBOLQSd47!&ç-:", null));
    }
}