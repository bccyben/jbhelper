package com.github.bccyben.common.csvConverter;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.ConverterDate;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import java.time.LocalDate;

/**
 * csvの変換をする際、変換失敗のデータはnullにする。これによって、変換途中で例外により中止することがない
 */
@Slf4j
public class NoErrorButNull<T, I> extends AbstractBeanField<T, I> {

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        final Class<?> fieldType = field.getType();
        if (!StringUtils.isNotBlank(value)) {
            return null;
        }
        if (Number.class.isAssignableFrom(fieldType)) {
            try {
                return ConvertUtils.convert(value, fieldType);
            } catch (Exception e) {
                log.warn("failed to convert csv field");
                return null;
            }
        }
        if (fieldType == LocalDate.class) {
            ConverterDate converter = new ConverterDate(fieldType, null, null, null, "yyyy-M-d",
                    "yyyy-M-d", "ISO", "ISO");
            try {
                final String value2 = value.replaceAll("/", "-");
                return converter.convertToRead(value2);
            } catch (Exception e) {
                log.warn("failed to convert csv field");
                return null;
            }
        }
        if (fieldType == Boolean.class) {
            switch (value.toLowerCase()) {
                case "true":
                    return true;
                case "false":
                    return false;
                default:
                    log.warn("failed to convert csv field");
                    return null;
            }
        }
        if (fieldType == String.class) {
            return value;
        }
        return null;
    }

}
