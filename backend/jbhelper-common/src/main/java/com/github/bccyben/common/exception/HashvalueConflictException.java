package com.github.bccyben.common.exception;

public class HashvalueConflictException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7008099314816875152L;

    public HashvalueConflictException(String message) {
        super("Hashvalue is conflict." + message);
    }
}
