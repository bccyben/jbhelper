package com.github.bccyben.common.exception;

/**
 * 想定したデータが見つかりません例外
 */
public class ExpectDataNotFound extends RuntimeException {
    public ExpectDataNotFound() {
        super();
    }

    public ExpectDataNotFound(String message) {
        super(message);
    }

    public ExpectDataNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
