package com.aleos.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * A global exception handler for handling various exceptions thrown by the application.
 * This class provides methods to handle specific exceptions and return appropriate views
 * with error messages.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationServiceException.class)
    public String handleUserNotFoundException(AuthenticationServiceException ex, Model model) {
        log.error(ex.getMessage(), ex);
        model.addAttribute("errorMessage", ex.getMessage());
        return "auth/sign-in";
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        log.error(ex.getMessage(), ex);
        model.addAttribute("errorMessage", "An unexpected error occurred.");
        return "error/general-error";
    }
}
