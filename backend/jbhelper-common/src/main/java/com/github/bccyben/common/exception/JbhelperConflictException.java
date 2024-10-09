package com.github.bccyben.common.exception;

/**
 * 排他チェック例外
 */
public class JbhelperConflictException extends RuntimeException {
    public JbhelperConflictException() {
        super();
    }

    public JbhelperConflictException(String message) {
        super(message);
    }

    public JbhelperConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
