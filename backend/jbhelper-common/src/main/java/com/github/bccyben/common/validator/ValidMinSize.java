package com.github.bccyben.common.validator;

import jakarta.validation.*;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ConstraintComposition(CompositionType.OR)
@ReportAsSingleViolation
@Constraint(validatedBy = {ValidMinSize.Validator.class})
public @interface ValidMinSize {
    String message() default "{validator.min.size}";

    String field();

    int min();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidMinSize, Collection<? extends Object>> {

        private int min;

        @Override
        public void initialize(ValidMinSize annotation) {
            this.min = annotation.min();
        }

        @Override
        public boolean isValid(Collection<? extends Object> list,
                               ConstraintValidatorContext constraintValidatorContext) {
            if (list == null) {
                return true;
            }
            return list.size() >= min;
        }
    }
}
