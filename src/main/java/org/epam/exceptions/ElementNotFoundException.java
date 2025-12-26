package org.epam.exceptions;

/**
 * Exception thrown when a web element cannot be found on the page.
 *
 * When to throw: Element doesn't exist, locator is wrong, or wait timeout
 *
 * Example:
 * throw new ElementNotFoundException(
 *     "Login button not found after 3 retries. Locator: id=login-button"
 * );
 */
public class ElementNotFoundException extends FrameworkException {

    /**
     * Constructor with message
     * @param message describes which element was not found
     */
    public ElementNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message describes which element was not found
     * @param cause the original Selenium exception
     */
    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

