package com.github.bccyben.common.exception;

public class InvalidLedgerException extends RuntimeException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6060483392739186008L;

    public InvalidLedgerException(String message) {
        super("Hyperledger fabric " + message);
    }
}
