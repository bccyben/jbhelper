package com.github.bccyben.common.validator;

import jakarta.validation.*;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.springframework.util.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.Collection;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ConstraintComposition(CompositionType.OR)
@ReportAsSingleViolation
@Constraint(validatedBy = {ValidMust.Validator.class})
public @interface ValidMust {

    String message() default "{validator.must}";

    String field();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidMust, Object> {
        @Override
        public void initialize(ValidMust validMust) {

        }


        @SuppressWarnings("rawtypes")
        @Override
        public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
            if (object == null) {
                return false;
            }
            if (object instanceof String) {
                return StringUtils.hasText((String) object);
            }
            if (object instanceof Collection) {
                return ((Collection) object).size() > 0;
            }
            if (object.getClass().isArray()) {
                return Array.getLength(object) > 0;
            }
            return true;
        }
    }
}
