package com.aleos.exception;

/**
 * The DropJarException class serves as the base class for exceptions related to the DropJar system.
 * This runtime exception can be utilized for various error scenarios within the DropJar application
 * or its related services.
 */
public class DropJarException extends RuntimeException {

    public DropJarException() {
        super();
    }

    public DropJarException(String message) {
        super(message);
    }

    public DropJarException(String message, Throwable cause) {
        super(message, cause);
    }

    public DropJarException(Throwable cause) {
        super(cause);
    }

    protected DropJarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
