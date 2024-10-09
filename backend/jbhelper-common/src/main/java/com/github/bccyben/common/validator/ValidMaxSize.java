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
@Constraint(validatedBy = {ValidMaxSize.Validator.class})
public @interface ValidMaxSize {
    String message() default "{validator.max.size}";

    String field();

    int max();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidMaxSize, Collection<? extends Object>> {

        private int max;

        @Override
        public void initialize(ValidMaxSize annotation) {
            this.max = annotation.max();
        }

        @Override
        public boolean isValid(Collection<? extends Object> list, ConstraintValidatorContext constraintValidatorContext) {
            if (list == null) {
                return true;
            }
            return list.size() <= max;
        }
    }
}
