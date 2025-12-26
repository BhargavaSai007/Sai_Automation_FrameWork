package org.epam.exceptions;

/**
 * Exception thrown when a wait operation exceeds the timeout duration.
 *
 * When to throw: Element didn't appear within specified wait time
 *
 * Example:
 * throw new TimeoutException(
 *     "Waited 30 seconds for element to be visible, but timeout occurred"
 * );
 */
public class TimeoutException extends FrameworkException {

    /**
     * Constructor with message
     * @param message describes what we were waiting for and timeout details
     */
    public TimeoutException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message describes what we were waiting for
     * @param cause the original Selenium TimeoutException
     */
    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}

