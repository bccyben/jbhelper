package com.github.bccyben.common.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.github.bccyben.common.cons.UserRole;

@Converter
public class UserRoleConverter implements AttributeConverter<UserRole, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRole attribute) {
        return attribute.getId();
    }

    @Override
    public UserRole convertToEntityAttribute(Integer dbData) {
        return UserRole.ofId(dbData);
    }

}
