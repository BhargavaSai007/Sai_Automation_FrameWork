package org.epam.exceptions;

/**
 * Exception thrown when page navigation fails.
 *
 * When to throw: Cannot navigate to URL, page didn't load properly
 *
 * Example:
 * throw new NavigationException(
 *     "Failed to navigate to https://www.saucedemo.com - connection timeout"
 * );
 */
public class NavigationException extends FrameworkException {

    /**
     * Constructor with message
     * @param message describes which URL we tried to navigate to
     */
    public NavigationException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message describes which URL we tried to navigate to
     * @param cause the original exception
     */
    public NavigationException(String message, Throwable cause) {
        super(message, cause);
    }
}

