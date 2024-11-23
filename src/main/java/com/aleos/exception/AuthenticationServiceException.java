package com.aleos.exception;

/**
 * This exception is thrown when there is an issue with the authentication service.
 * It extends from the {@link DropJarException}, allowing the capture of additional context or root cause.
 */
public class AuthenticationServiceException extends DropJarException {

    public AuthenticationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationServiceException(String message) {
        super(message);
    }
}
