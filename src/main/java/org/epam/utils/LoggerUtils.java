package org.epam.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logger Utility Class - Makes logging easier to use throughout the framework
 *
 * Usage:
 * private static final Logger logger = LoggerUtils.getLogger(LoginPage.class);
 * logger.info("User logged in successfully");
 * logger.debug("Button clicked at coordinates: 100, 200");
 * logger.error("Login failed");
 */
public class LoggerUtils {

    /**
     * Get a logger for the given class
     * @param clazz the class that wants to log
     * @return Logger instance for that class
     *
     * Example:
     * Logger logger = LoggerUtils.getLogger(LoginPage.class);
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * Get a logger for the given class name (String)
     * @param className the name of the class
     * @return Logger instance
     */
    public static Logger getLogger(String className) {
        return LoggerFactory.getLogger(className);
    }

    /**
     * Log test step at INFO level with timestamp
     * Useful for marking important test actions
     *
     * Example:
     * LoggerUtils.logTestStep("User successfully logged in");
     */
    public static void logTestStep(String stepDescription) {
        Logger logger = LoggerFactory.getLogger("TestSteps");
        logger.info("===== TEST STEP: {} =====", stepDescription);
    }

    /**
     * Log error with stack trace
     * @param logger the logger instance
     * @param message error message
     * @param exception the exception
     *
     * Example:
     * try {
     *     // code
     * } catch (Exception e) {
     *     LoggerUtils.logError(logger, "Login failed", e);
     * }
     */
    public static void logError(Logger logger, String message, Exception exception) {
        logger.error(message, exception);
    }

    /**
     * Log assertion failure
     * @param logger the logger instance
     * @param expected what we expected
     * @param actual what we got instead
     */
    public static void logAssertionFailure(Logger logger, String expected, String actual) {
        logger.error("Assertion Failed! Expected: {}, but got: {}", expected, actual);
    }
}

