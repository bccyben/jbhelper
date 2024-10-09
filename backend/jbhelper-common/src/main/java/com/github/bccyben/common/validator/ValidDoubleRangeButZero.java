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
@Constraint(validatedBy = {ValidDoubleRangeButZero.Validator.class})
public @interface ValidDoubleRangeButZero {

    String message() default "{validator.double.range}";

    String field();

    double min() default 0.01d;

    double max() default 9999999999.99d;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidDoubleRangeButZero, Double> {

        private double min;

        private double max;

        @Override
        public void initialize(ValidDoubleRangeButZero annotation) {
            this.min = annotation.min();
            this.max = annotation.max();
        }

        @Override
        public boolean isValid(Double num, ConstraintValidatorContext constraintValidatorContext) {
            if (num == null) {
                return true;
            }
            if (num == 0) {
                return true;
            }
            return num >= min && num <= max;
        }
    }
}
