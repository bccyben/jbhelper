package com.github.bccyben.common.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import com.github.bccyben.common.exception.InvalidRequestException;

import java.util.Set;

/**
 * validatorで検証可能にしたいモデルに継承
 */
public abstract class Validatable {
    public void validate(Validator validator, Class<?>... groups) {
        Set<ConstraintViolation<Validatable>> errors = validator.validate(this, groups);
        if (!errors.isEmpty()) {
            StringBuilder messages = new StringBuilder();
            errors.forEach(error -> {
                messages.append(messages.length() == 0 ? error.getMessage() : '\n' + error.getMessage());
            });
            throw new InvalidRequestException(messages.toString());
        }
    }
}
