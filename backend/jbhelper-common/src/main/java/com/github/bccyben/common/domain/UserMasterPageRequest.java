package com.github.bccyben.common.domain;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class UserMasterPageRequest extends CustomPageRequest {

        public UserMasterPageRequest(Integer pageNum, Integer pageSize, String sort, String order) {
                super(pageNum, pageSize, sort, order);
        }

        @Override
        protected String mapProperty(String propertyInRequest) {
                return propertyInRequest;
        }

        @Override
        public PageRequest toPageable() {
                String mappedProperty = mapProperty(sort);
                if (mappedProperty == null || mappedProperty.isEmpty()) {
                        return PageRequest.of(page, size);
                }
                Boolean isAsc = order.toUpperCase().equals("ASC");
                switch (mappedProperty) {
                        case "userName":
                                Sort nameSorts = Sort.by(
                                                isAsc ? Sort.Order.asc("familyName")
                                                                : Sort.Order.desc("familyName"),
                                                isAsc ? Sort.Order.asc("givenName")
                                                                : Sort.Order.desc("givenName"),
                                                Sort.Order.desc("updateTime"));
                                return PageRequest.of(page, size, nameSorts);
                        default:
                                Sort defaultSorts = Sort.by(
                                                isAsc ? Sort.Order.asc("updateTime")
                                                                : Sort.Order.desc("updateTime"));
                                return PageRequest.of(page, size, defaultSorts);
                }
        }

}
