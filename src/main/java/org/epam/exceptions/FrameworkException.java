package org.epam.exceptions;

/**
 * Base custom exception for the test automation framework.
 * All framework-specific exceptions extend from this class.
 *
 * Usage: Catch this to handle all framework errors
 * Example: catch (FrameworkException e) { ... }
 */
public class FrameworkException extends RuntimeException {

    /**
     * Constructor with message only
     * @param message describes what went wrong
     */
    public FrameworkException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message describes what went wrong
     * @param cause the original exception that caused this
     */
    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with cause only
     * @param cause the original exception that caused this
     */
    public FrameworkException(Throwable cause) {
        super(cause);
    }
}

