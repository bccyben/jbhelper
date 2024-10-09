package com.github.bccyben.common.validator;

import jakarta.validation.*;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ConstraintComposition(CompositionType.OR)
@ReportAsSingleViolation
@Constraint(validatedBy = {ValidCollectionRegex.Validator.class})
public @interface ValidCollectionRegex {
    String message() default "{validator.invalid.object}";

    String field();

    String regex();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements
            ConstraintValidator<ValidCollectionRegex, Collection<? extends Object>> {

        private String regex;

        @Override
        public void initialize(ValidCollectionRegex annotation) {
            this.regex = annotation.regex();
        }

        @Override
        public boolean isValid(Collection<? extends Object> list,
                               ConstraintValidatorContext constraintValidatorContext) {
            Pattern pattern = Pattern.compile(regex);
            for (var obj : list) {
                if (obj == null) {
                    return true;
                }
                if (obj instanceof Double) {
                    Matcher matcher = pattern.matcher(BigDecimal.valueOf((Double) obj).toPlainString());
                    if (!matcher.find()) {
                        return false;
                    }
                }
                if (obj instanceof String) {
                    Matcher matcher = pattern.matcher((String) obj);
                    if (!matcher.find()) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
