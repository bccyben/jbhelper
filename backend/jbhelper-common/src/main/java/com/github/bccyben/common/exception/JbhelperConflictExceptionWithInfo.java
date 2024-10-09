package com.github.bccyben.common.exception;

import java.util.List;

import lombok.Getter;

import java.util.ArrayList;

/**
 * 排他チェック例外、infoつき
 */
public class JbhelperConflictExceptionWithInfo extends JbhelperConflictException {
    @Getter
    private List<String> info = new ArrayList<>();

    public JbhelperConflictExceptionWithInfo() {
        super();
    }

    public JbhelperConflictExceptionWithInfo(String message, List<String> info) {
        super(message);
        this.info = info;
    }

    public JbhelperConflictExceptionWithInfo(String message, Throwable cause) {
        super(message, cause);
    }
}
