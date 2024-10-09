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
@Constraint(validatedBy = {ValidIntegerValues.Validator.class})
public @interface ValidIntegerValues {
    String message() default "{validator.invalid.object}";

    String field();

    int[] values();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidIntegerValues, Integer> {

        private int[] values;

        @Override
        public void initialize(ValidIntegerValues annotation) {
            this.values = annotation.values();
        }

        @Override
        public boolean isValid(Integer target, ConstraintValidatorContext constraintValidatorContext) {
            if (target == null) {
                return true;
            }
            for (var value : values) {
                if (target.equals(value)) {
                    return true;
                }
            }

            return false;
        }
    }
}
