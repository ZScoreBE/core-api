package be.zsoft.zscore.core.validation.validator;

import be.zsoft.zscore.core.validation.annotation.PasswordStrength;
import com.nulabinc.zxcvbn.Zxcvbn;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {

    private int minScore;

    @Override
    public void initialize(PasswordStrength constraintAnnotation) {
        minScore = constraintAnnotation.minScore();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        Zxcvbn zxcvbn = new Zxcvbn();
        int score = zxcvbn.measure(password).getScore();
        return score >= minScore;
    }
}
