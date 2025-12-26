package org.epam.exceptions;

/**
 * Exception thrown when configuration loading or reading fails.
 *
 * When to throw: Config file missing, invalid property value, environment setup issue
 *
 * Example:
 * throw new ConfigurationException(
 *     "Configuration file not found at: src/test/resources/config.properties"
 * );
 */
public class ConfigurationException extends FrameworkException {

    /**
     * Constructor with message
     * @param message describes what configuration issue occurred
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message describes what configuration issue occurred
     * @param cause the original exception
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

