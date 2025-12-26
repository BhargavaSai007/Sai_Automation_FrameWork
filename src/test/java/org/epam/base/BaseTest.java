package org.epam.base;

import org.epam.config.ConfigManager;
import org.epam.driver.DriverManager;
import org.epam.exceptions.FrameworkException;
import org.epam.utils.TestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeSuite
    public void suiteSetup() {
        System.out.println("=== Test Suite Started ===");
        System.out.println("Base URL: " + ConfigManager.getBaseUrl());
        System.out.println("Browser: " + ConfigManager.getBrowser());
        System.out.println("========================");
    }

    @BeforeMethod
    public void setUp() {
        try {
            TestUtils.logTestStep("Setting up test environment");
            logger.info("Initializing WebDriver for test");

            try {
                DriverManager.createDriver();
                logger.info("WebDriver created successfully");
            } catch (FrameworkException e) {
                logger.error("Framework exception during driver creation: {}", e.getMessage());
                throw e;
            } catch (Exception e) {
                logger.error("Unexpected exception during driver creation: {}", e.getMessage(), e);
                throw new FrameworkException("Failed to create WebDriver: " + e.getMessage(), e);
            }

            TestUtils.logTestStep("Browser launched successfully");
            logger.info("Test setup completed successfully");
        } catch (FrameworkException e) {
            logger.error("Test setup failed with framework exception: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Test setup failed with unexpected exception: {}", e.getMessage(), e);
            throw new FrameworkException("Test setup failed: " + e.getMessage(), e);
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            TestUtils.logTestStep("Cleaning up test environment");
            logger.info("Closing WebDriver");

            try {
                DriverManager.quitDriver();
                logger.info("WebDriver closed successfully");
            } catch (FrameworkException e) {
                logger.error("Framework exception during driver cleanup: {}", e.getMessage());
                // Don't throw in tearDown - we want to continue cleanup
                // But log the error for visibility
            } catch (Exception e) {
                logger.error("Unexpected exception during driver cleanup: {}", e.getMessage(), e);
                // Don't throw in tearDown
            }

            TestUtils.logTestStep("Browser closed successfully");
            logger.info("Test cleanup completed");
        } catch (Exception e) {
            logger.error("Unexpected exception in tearDown: {}", e.getMessage(), e);
            // Don't throw from tearDown
        }
    }

    @AfterSuite
    public void cleanBrowserProcess(){

        try {
            // Kill ChromeDriver and Chrome
            Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
            Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");

            // Kill GeckoDriver and Firefox
            Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
            Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");

            // Kill EdgeDriver and Edge
             Runtime.getRuntime().exec("taskkill /F /IM msedgedriver.exe");
             Runtime.getRuntime().exec("taskkill /F /IM msedge.exe");

            System.out.println("All browser drivers and browsers killed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Helper method for taking screenshots in tests
    protected String takeScreenshot(String testName) {
        return TestUtils.takeScreenshot(testName);
    }

    // Helper method for logging test steps
    protected void logStep(String stepDescription) {
        TestUtils.logTestStep(stepDescription);
    }
}