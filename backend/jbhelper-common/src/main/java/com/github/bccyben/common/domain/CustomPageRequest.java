package com.github.bccyben.common.domain;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

/**
 * ページングをする際に継承させる抽象クラス
 */
@Getter
public abstract class CustomPageRequest {

    private static final int DEFAULT_SIZE = 20;

    @Parameter(description = "Zero-based page index", example = "2", schema = @Schema(type = "integer", defaultValue = "0"))
    protected final int page;

    @Parameter(description = "ページサイズ", example = "10", schema = @Schema(type = "integer", defaultValue = "20"))
    protected final int size;

    @Parameter(schema = @Schema(type = "string"), description = "並び替え項目。項目名はレスポンスを参照。")
    protected final String sort;

    @Parameter(schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}), description = "並び順")
    protected final String order;

    public CustomPageRequest(Integer page, Integer size, String sort, String order) {
        this.page = page != null ? page : 0;
        this.size = size != null ? size : DEFAULT_SIZE;
        this.sort = sort;
        this.order = order;
    }

    public PageRequest toPageable() {
        String mappedProperty = mapProperty(sort);
        if (mappedProperty == null || mappedProperty.isEmpty()) {
            return PageRequest.of(page, size);
        }
        Optional<Sort.Direction> direction = Sort.Direction.fromOptionalString(order);
        Sort s = direction.map(value -> Sort.by(value, mappedProperty)).orElseGet(() -> Sort.by(mappedProperty));
        return PageRequest.of(page, size, s);
    }

    public PageRequest toPageable(int noLimitRow) {
        String mappedProperty = mapProperty(sort);
        if (mappedProperty == null || mappedProperty.isEmpty()) {
            return PageRequest.of(page, noLimitRow);
        }
        Optional<Sort.Direction> direction = Sort.Direction.fromOptionalString(order);
        Sort s = direction.map(value -> Sort.by(value, mappedProperty)).orElseGet(() -> Sort.by(mappedProperty));
        return PageRequest.of(page, noLimitRow, s);
    }

    protected abstract String mapProperty(String propertyInRequest);
}
