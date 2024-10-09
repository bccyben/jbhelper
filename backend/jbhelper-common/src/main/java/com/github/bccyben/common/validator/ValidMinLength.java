package com.github.bccyben.common.validator;

import jakarta.validation.*;
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
@Constraint(validatedBy = {ValidMinLength.Validator.class})
public @interface ValidMinLength {
    String message() default "{validator.min.length}";

    String field();

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidMinLength, String> {

        private int min;

        @Override
        public void initialize(ValidMinLength annotation) {
            this.min = annotation.min();
        }

        @Override
        public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
            if (text == null) {
                return true;
            }
            return text.length() >= min;
        }
    }
}
