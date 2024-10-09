package com.github.bccyben.common.validator;

import jakarta.validation.*;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ConstraintComposition(CompositionType.OR)
@ReportAsSingleViolation
@Constraint(validatedBy = {ValidFutureDate.Validator.class})
public @interface ValidFutureDate {

    String message() default "未来日付の指定はできません。";

    // String field();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidFutureDate, LocalDate> {

        @Override
        public void initialize(ValidFutureDate validFutureDate) {

        }

        @Override
        public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
            if (date == null) {
                return true;
            }
            return !date.isAfter(LocalDate.now());
        }
    }
}
