package org.epam.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple test to verify logging is working
 * Run this class to test if logging configuration is working properly
 */
public class LoggingTest {

    private static final Logger logger = LoggerFactory.getLogger(LoggingTest.class);

    public static void main(String[] args) {
        System.out.println("=== Testing Logging Configuration ===");
        System.out.println("This will test both console and file logging...\n");

        // Test different log levels
        logger.debug("DEBUG: This is a debug message - should appear in file only");
        logger.info("INFO: This is an info message - should appear in both console and file");
        logger.warn("WARN: This is a warning message");
        logger.error("ERROR: This is an error message");

        // Test specific package logging
        Logger frameworkLogger = LoggerFactory.getLogger("org.epam.pages.LoginPage");
        frameworkLogger.debug("DEBUG from framework package - should be visible");
        frameworkLogger.info("INFO from framework package");

        System.out.println("\n=== Test completed ===");
        System.out.println("✅ Console output: You should see INFO, WARN, ERROR messages above");
        System.out.println("✅ File output: Check target/logs/test-automation.log");
        System.out.println("✅ File should contain all messages including DEBUG level");
    }
}
