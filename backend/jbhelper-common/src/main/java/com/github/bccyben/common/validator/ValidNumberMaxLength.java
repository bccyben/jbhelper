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
@Constraint(validatedBy = {ValidNumberMaxLength.Validator.class})
public @interface ValidNumberMaxLength {
    String message() default "{validator.max.length}";

    String field();

    int max();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidNumberMaxLength, Number> {

        private int max;

        @Override
        public void initialize(ValidNumberMaxLength annotation) {
            this.max = annotation.max();
        }

        @Override
        public boolean isValid(Number number, ConstraintValidatorContext constraintValidatorContext) {
            if (number == null) {
                return true;
            }
            return number.toString().length() <= max;
        }
    }
}
