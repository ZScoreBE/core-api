package be.zsoft.zscore.core.validation.annotation;

import be.zsoft.zscore.core.validation.validator.PasswordStrengthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordStrengthValidator.class)
@Documented
public @interface PasswordStrength {

    int minScore() default 2;

    String message() default "{password.not.strong.enough}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
