package com.github.bccyben.common.model.csv;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public abstract class CsvLine {
    /**
     * 行番号
     */
    private Integer lineNumber;

    /**
     * エラー理由リスト
     */
    private List<String> errorList = new ArrayList<>();

    /**
     * 検証
     *
     * @param validator
     * @param groups
     */
    public void validate(Validator validator, Class<?>... groups) {
        Set<ConstraintViolation<CsvLine>> errors = validator.validate(this, groups);
        if (!errors.isEmpty()) {
            errors.forEach(error -> {
                this.getErrorList().add(error.getMessage());
            });
        }
    }

    public LocalDate stringToLocalDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            this.getErrorList().add("Invalid date");
            return null;
        }
    }
}
