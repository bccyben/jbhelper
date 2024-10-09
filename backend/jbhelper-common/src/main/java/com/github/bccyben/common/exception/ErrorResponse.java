package com.github.bccyben.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * エラーレスポンス
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    /**
     * エラーコード
     */
    private String code;

    /**
     * エラーメッセージ
     */
    private String message;

    /**
     * エラー情報
     */
    private List<String> info;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
