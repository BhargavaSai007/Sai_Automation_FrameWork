package org.epam.driver;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.epam.config.ConfigManager;
import org.epam.exceptions.ConfigurationException;
import org.epam.exceptions.FrameworkException;
import java.time.Duration;

/**
 * Driver Manager - Manages WebDriver lifecycle with ThreadLocal for parallel execution
 *
 * Design Patterns Used:
 * 1. Singleton Pattern: Single instance per thread (ThreadLocal)
 * 2. Factory Pattern: BrowserFactory creates drivers
 *
 * Usage:
 * DriverManager.createDriver();    // Creates driver (before each test)
 * WebDriver driver = DriverManager.getDriver();
 * DriverManager.quitDriver();      // Closes driver (after each test)
 *
 * Thread Safety:
 * ThreadLocal ensures each thread has its own WebDriver instance
 * Safe for parallel test execution
 */
public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);

    // ThreadLocal for parallel execution - each thread gets its own driver
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Get the WebDriver instance for current thread
     * @return WebDriver instance
     * @throws FrameworkException if driver not initialized
     */
    public static WebDriver getDriver() {
        WebDriver driverInstance = driver.get();

        if (driverInstance == null) {
            logger.error("WebDriver not initialized for current thread. Call DriverManager.createDriver() first.");
            throw new FrameworkException("WebDriver not initialized. Please ensure DriverManager.createDriver() was called in @BeforeMethod");
        }
        return driverInstance;
    }

    /**
     * Create WebDriver instance based on system property or config
     *
     * Called from BeforeMethod in BaseTest
     *
     * Priority:
     * 1. System property: -Dbrowser=firefox
     * 2. Config file: browser=chrome
     * 3. Default: CHROME
     *
     * Example usage:
     * mvn test -Dbrowser=firefox
     * mvn test -Dbrowser=edge
     */
    public static void createDriver() {
        logger.info("Initializing WebDriver");

        try {
            // Get browser type from system property or config
            String browserName = System.getProperty("browser", ConfigManager.getBrowser());
            logger.info("Browser requested: {}", browserName);

            if (browserName == null || browserName.trim().isEmpty()) {
                throw new ConfigurationException("Browser name is null or empty. Please set browser in config.properties or via -Dbrowser=<name>");
            }

            // Convert string to BrowserType enum
            BrowserType browserType = null;
            try {
                browserType = BrowserType.fromString(browserName);
                logger.info("Browser type resolved to: {}", browserType);
            } catch (IllegalArgumentException e) {
                throw new ConfigurationException("Invalid browser type: " + browserName + ". Supported: chrome, firefox, edge", e);
            }

            // Use factory to create driver
            WebDriver webDriver = null;
            try {
                webDriver = BrowserFactory.createDriver(browserType);
                logger.info("WebDriver created successfully for: {}", browserType);
            } catch (Exception e) {
                throw new FrameworkException("Failed to create WebDriver for browser: " + browserType + ". Ensure required drivers are installed.", e);
            }

            driver.set(webDriver);

            try {
                // Configure timeouts (in seconds)
                int timeout = ConfigManager.getTimeout();
                logger.debug("Setting implicit wait timeout: {} seconds", timeout);
                getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));

                // Configure page load timeout
                int pageLoadTimeout = 30;
                logger.debug("Setting page load timeout: {} seconds", pageLoadTimeout);
                getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));

                // Configure script timeout
                int scriptTimeout = 30;
                logger.debug("Setting script timeout: {} seconds", scriptTimeout);
                getDriver().manage().timeouts().scriptTimeout(Duration.ofSeconds(scriptTimeout));

                // Maximize window (unless in headless mode)
                if (!ConfigManager.isHeadless()) {
                    logger.debug("Maximizing browser window");
                    getDriver().manage().window().maximize();
                } else {
                    logger.debug("Headless mode enabled - skipping window maximization");
                }
            } catch (Exception e) {
                logger.error("Failed to configure WebDriver timeouts and window", e);
                throw new FrameworkException("Failed to configure WebDriver: " + e.getMessage(), e);
            }

            logger.info("WebDriver initialized successfully for: {}", browserName);

        } catch (FrameworkException e) {
            logger.error("Framework exception during driver creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected exception during driver creation: {}", e.getMessage(), e);
            throw new FrameworkException("Unexpected error while creating WebDriver: " + e.getMessage(), e);
        }
    }

    /**
     * Close WebDriver and cleanup ThreadLocal
     *
     * Called from AfterMethod in BaseTest
     *
     * Important: Must call driver.remove() to prevent memory leaks in parallel execution
     */
    public static void quitDriver() {
        try {
            WebDriver driverInstance = driver.get();
            if (driverInstance != null) {
                try {
                    logger.info("Closing WebDriver");
                    driverInstance.quit();
                    logger.debug("WebDriver quit successfully");
                } catch (Exception e) {
                    logger.error("Error while quitting WebDriver: {}", e.getMessage(), e);
                    // Don't throw - we still need to cleanup ThreadLocal
                    throw new FrameworkException("Failed to properly close WebDriver: " + e.getMessage(), e);
                }
            } else {
                logger.warn("WebDriver is null - skipping quit operation");
            }
        } catch (FrameworkException e) {
            logger.error("Framework exception during driver cleanup: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected exception during driver quit: {}", e.getMessage(), e);
        } finally {
            try {
                // Always remove ThreadLocal to prevent memory leaks
                driver.remove();
                logger.debug("ThreadLocal WebDriver removed successfully");
            } catch (Exception e) {
                logger.error("Error while removing ThreadLocal driver: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Check if WebDriver is running
     * Useful for debugging
     * @return true if driver exists and is not null
     */
    public static boolean isDriverActive() {
        return getDriver() != null;
    }
}