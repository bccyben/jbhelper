package com.github.bccyben.common.validator;

import jakarta.validation.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ConstraintComposition(CompositionType.OR)
@ReportAsSingleViolation
@Constraint(validatedBy = {ValidRegex.Validator.class})
public @interface ValidRegex {

    String message() default "{validator.invalid.object}";

    String field();

    String regex();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidRegex, Object> {

        private String regex;

        @Override
        public void initialize(ValidRegex annotation) {
            this.regex = annotation.regex();
        }

        @Override
        public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
            Pattern pattern = Pattern.compile(regex);
            if (obj == null || StringUtils.isBlank(obj.toString())) {
                return true;
            }
            if (obj instanceof Double) {
                Matcher matcher = pattern.matcher(BigDecimal.valueOf((Double) obj).toPlainString());
                return matcher.find();
            }
            if (obj instanceof String) {
                Matcher matcher = pattern.matcher((String) obj);
                return matcher.find();
            }
            return false;
        }
    }
}
