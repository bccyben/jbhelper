package com.github.bccyben.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JbhelperException extends RuntimeException {

    /**
     * Httpエラーコード
     */
    private HttpStatus status;

    /**
     * エラーメッセージ
     */
    private String errorMessage;

    /**
     * コンストラクター
     *
     * @param status Httpステータス
     */
    public JbhelperException(HttpStatus status) {
        this(status, "", null);
    }

    /**
     * コンストラクター(デフォルトが「500」)
     *
     * @param errorMessage エラーメッセージ
     */
    public JbhelperException(String errorMessage) {

        this(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }

    /**
     * コンストラクター
     *
     * @param errorMessage エラーメッセージ
     * @param cause        実際発生した例外
     */
    public JbhelperException(String errorMessage, Throwable cause) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, cause);
    }

    /**
     * コンストラクター
     *
     * @param status       Httpステータス
     * @param errorMessage 実際発生した例外
     */
    public JbhelperException(HttpStatus status, String errorMessage) {
        this(status, errorMessage, null);
    }

    /**
     * コンストラクター
     *
     * @param status       Httpステータス
     * @param errorMessage エラーメッセージ
     * @param cause        実際発生した例外
     */
    public JbhelperException(HttpStatus status, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
