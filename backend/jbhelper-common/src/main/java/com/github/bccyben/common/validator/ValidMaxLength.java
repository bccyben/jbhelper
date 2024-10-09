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
@Constraint(validatedBy = {ValidMaxLength.Validator.class})
public @interface ValidMaxLength {

    String message() default "{validator.max.length}";

    String field();

    int max();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidMaxLength, String> {

        private int max;

        @Override
        public void initialize(ValidMaxLength annotation) {
            this.max = annotation.max();
        }

        @Override
        public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
            if (StringUtils.isEmpty(text)) {
                return true;
            }
            return text.length() <= max;
        }
    }
}
