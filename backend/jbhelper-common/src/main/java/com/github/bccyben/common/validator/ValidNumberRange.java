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
@Constraint(validatedBy = {ValidNumberRange.Validator.class})
public @interface ValidNumberRange {
    String message() default "{validator.double.range}";

    String field();

    double min() default 0.01d;

    double max() default 9999999999.99d;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidNumberRange, Number> {

        private Double min;

        private Double max;

        @Override
        public void initialize(ValidNumberRange annotation) {
            this.min = annotation.min();
            this.max = annotation.max();
        }

        @Override
        public boolean isValid(Number num, ConstraintValidatorContext constraintValidatorContext) {
            if (num == null) {
                return true;
            }
            if (num instanceof Integer) {
                return (Integer) num >= min.intValue() && (Integer) num <= max.intValue();
            }
            if (num instanceof Double) {
                return (Double) num >= min && (Double) num <= max;
            }
            return false;
        }
    }
}
