package com.github.bccyben.common.mappingStrategy;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.github.bccyben.common.annotation.EnableNotDisplayHeaderCsv;
import com.github.bccyben.common.annotation.NotDisplayHeaderCsv;
import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CustomBeanToCSVMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {


    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        final int numColumns = getFieldMap().values().size();
        super.generateHeader(bean);
        String[] header = new String[numColumns];
        BeanField beanField;
        for (int i = 0; i < numColumns; i++) {
            beanField = findField(i);
            String columnHeaderName = extractHeaderName(beanField);
            header[i] = columnHeaderName;
        }
        return header;
    }


    private String extractHeaderName(final BeanField beanField) {
        if (beanField == null || beanField.getField() == null
                || (beanField.getField().getDeclaredAnnotationsByType(
                CsvCustomBindByName.class).length == 0
                && beanField.getField().getDeclaredAnnotationsByType(CsvBindByName.class).length == 0)) {
            return StringUtils.EMPTY;
        }
        final Boolean isCsvCustomBindByName = beanField.getField().getDeclaredAnnotationsByType(
                CsvCustomBindByName.class).length != 0;
        if (isCsvCustomBindByName) {
            final CsvCustomBindByName customBindByNameAnnotation = beanField.getField()
                    .getDeclaredAnnotationsByType(CsvCustomBindByName.class)[0];
            return customBindByNameAnnotation.column();
        }
        final CsvBindByName bindByNameAnnotation = beanField.getField()
                .getDeclaredAnnotationsByType(CsvBindByName.class)[0];
        return bindByNameAnnotation.column();
    }

    /**
     * make {@link NotDisplayHeaderCsv} work
     * make sure use {@link EnableNotDisplayHeaderCsv}
     */
    @Override
    protected void loadAnnotatedFieldMap(ListValuedMap<Class<?>, Field> fields) {
        Boolean isEnableNotDisplayHeaderCsv = this.type
                .getAnnotationsByType(EnableNotDisplayHeaderCsv.class).length > 0;
        if (!isEnableNotDisplayHeaderCsv) {
            super.loadAnnotatedFieldMap(fields);
            return;
        }
        Integer index = 0;
        List<Entry<Class<?>, Field>> list = fields.entries().stream()
                .filter(f -> !f.getValue().isAnnotationPresent(
                        NotDisplayHeaderCsv.class))
                .sorted((c1, c2) -> c1.getValue().getAnnotationsByType(CsvBindByPosition.class)[0].position() - c2
                        .getValue().getAnnotationsByType(CsvBindByPosition.class)[0].position())
                .toList();
        for (Map.Entry<Class<?>, Field> classAndField : list) {
            Class<?> localType = classAndField.getKey();
            Field localField = classAndField.getValue();
            CsvBindByPosition annotation = selectAnnotationForProfile(
                    localField.getAnnotationsByType(CsvBindByPosition.class),
                    CsvBindByPosition::profiles);
            if (annotation != null) {
                try {
                    registerBinding(annotation, localType, localField, index);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            index++;
        }
    }

    /**
     * binding the data
     *
     * @param annotation
     * @param localType
     * @param localField
     * @param index
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    private void registerBinding(CsvBindByPosition annotation, Class<?> localType, Field localField, Integer index)
            throws IllegalAccessException {
        var map = (FieldMapByPosition<T>) FieldUtils.readField(this, "fieldMap", true);
        String fieldLocale = annotation.locale();
        String fieldWriteLocale = annotation.writeLocaleEqualsReadLocale()
                ? fieldLocale
                : annotation.writeLocale();
        CsvConverter converter = determineConverter(localField, localField.getType(), fieldLocale, fieldWriteLocale,
                null);
        map.put(index, new BeanFieldSingleValue<>(
                localType, localField, annotation.required(), errorLocale,
                converter, annotation.capture(), annotation.format()));
    }

}
