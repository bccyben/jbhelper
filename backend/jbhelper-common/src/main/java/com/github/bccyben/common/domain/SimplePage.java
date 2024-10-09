package com.github.bccyben.common.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimplePage<T> {

    @Schema(description = "ページ内容")
    private final List<T> content;

    @Schema(description = "総数", type = "integer")
    private final long totalElements;

    public SimplePage(Page<T> page) {
        content = page.getContent();
        totalElements = page.getTotalElements();
    }

    public SimplePage(List<T> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public <U> SimplePage<U> map(Function<? super T, ? extends U> converter) {
        return new SimplePage<>(content.stream().map(converter).collect(Collectors.toList()), totalElements);
    }

}
