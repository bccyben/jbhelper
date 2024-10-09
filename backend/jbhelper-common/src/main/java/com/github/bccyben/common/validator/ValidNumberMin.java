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
@Constraint(validatedBy = {ValidNumberMin.Validator.class})
public @interface ValidNumberMin {

    String message() default "{validator.number.min}";

    String field();

    double min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidNumberMin, Number> {

        private Double min;

        @Override
        public void initialize(ValidNumberMin annotation) {
            this.min = annotation.min();
        }

        @Override
        public boolean isValid(Number num, ConstraintValidatorContext constraintValidatorContext) {
            if (num == null) {
                return true;
            }
            return (double) num >= min;
        }
    }
}
