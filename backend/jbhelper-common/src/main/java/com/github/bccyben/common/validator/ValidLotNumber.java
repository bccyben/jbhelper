package com.github.bccyben.common.validator;

import jakarta.validation.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ConstraintComposition(CompositionType.OR)
@ReportAsSingleViolation
@Constraint(validatedBy = {ValidLotNumber.Validator.class})
public @interface ValidLotNumber {

    String message() default "{validator.lot.number}";

    String field();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidLotNumber, String> {

        @Override
        public void initialize(ValidLotNumber validLotNumber) {

        }

        @Override
        public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
            if (StringUtils.isEmpty(text)) {
                return true;
            }
            return text.matches("^[a-zA-Z0-9-_-]*$");
        }
    }
}
