package org.epam.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.epam.config.ConfigManager;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Browser Factory - Creates WebDriver instances for different browsers
 * <p>
 * Factory Pattern: Encapsulates the creation logic of WebDriver objects
 * Benefits:
 * - Centralized browser configuration
 * - Easy to add new browsers
 * - Single place to modify browser options
 * - Separates creation logic from usage
 * <p>
 * Usage:
 * WebDriver driver = BrowserFactory.createDriver(BrowserType.CHROME);
 */
public class BrowserFactory {
    private static final Logger logger = LoggerFactory.getLogger(BrowserFactory.class);

    /**
     * Create and return WebDriver instance for specified browser
     *
     * @param browserType the browser type (CHROME, FIREFOX, EDGE)
     * @return configured WebDriver instance
     * <p>
     * Example:
     * WebDriver driver = BrowserFactory.createDriver(BrowserType.FIREFOX);
     */
    public static WebDriver createDriver(BrowserType browserType) {
        logger.info("Creating WebDriver for browser: {}", browserType.getBrowserName());

        WebDriver driver;

        switch (browserType) {
            case CHROME:
                driver = createChromeDriver();
                break;
            case FIREFOX:
                driver = createFirefoxDriver();
                break;
            case EDGE:
                driver = createEdgeDriver();
                break;
            default:
                logger.warn("Unknown browser type: {}, defaulting to Chrome", browserType);
                driver = createChromeDriver();
                break;
        }

        logger.info("WebDriver created successfully for: {}", browserType.getBrowserName());
        return driver;
    }

    /**
     * Create Chrome WebDriver with optimized options
     *
     * @return configured ChromeDriver instance
     */
    private static WebDriver createChromeDriver() {
        logger.debug("Setting up Chrome WebDriver");

        // Setup ChromeDriver binary automatically
        WebDriverManager.chromedriver().setup();
        logger.debug("ChromeDriver binary setup complete");

        // Create Chrome options
        ChromeOptions options = new ChromeOptions();

        // Add common options for stability
        addCommonOptions(options);

        // Chrome-specific options
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-password-generation");
        options.addArguments("--disable-password-manager-reauthentication");

        // Disable automation detection
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Add headless mode if configured
        if (ConfigManager.isHeadless()) {
            logger.info("Chrome running in headless mode");
            options.addArguments("--headless=new");
        }

        logger.debug("Chrome options configured");
        return new ChromeDriver(options);
    }

    /**
     * Create Firefox WebDriver with optimized options
     *
     * @return configured FirefoxDriver instance
     */
    private static WebDriver createFirefoxDriver() {
        logger.debug("Setting up Firefox WebDriver");

        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");

        logger.debug("GeckoDriver binary setup complete");

// Create Firefox options
        FirefoxOptions options = new FirefoxOptions();

// Specify Firefox binary path if not in default location
        options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe"); // Update path

// Add common options for stability
        addCommonOptions(options);

// Firefox-specific options
        options.addArguments("--disable-notifications");

// Add headless mode if configured
        if (ConfigManager.isHeadless()) {
            logger.info("Firefox running in headless mode");
            options.addArguments("--headless");
        }

        logger.debug("Firefox options configured");
        return new FirefoxDriver(options);

    }

    /**
     * Create Edge WebDriver with optimized options
     *
     * @return configured EdgeDriver instance
     */
    private static WebDriver createEdgeDriver() {
        logger.debug("Setting up Edge WebDriver");

// Set EdgeDriver mirror URL before setup
        System.setProperty("SE_MSEDGEDRIVER_MIRROR_URL", "https://msedgedriver.microsoft.com");

// Setup EdgeDriver binary automatically

        EdgeOptions options = new EdgeOptions();

        options.addArguments("--start-maximized");
        //Microsoft has changed the URL for the Edge WebDriver download location

        options.addArguments("--start-maximized");

// Add common options for stability
        addCommonOptions(options);

// Edge-specific options
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");

// Disable automation detection
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.addArguments("--disable-blink-features=AutomationControlled");

// Add headless mode if configured
        if (ConfigManager.isHeadless()) {
            logger.info("Edge running in headless mode");
            // options.addArguments("--headless=new");
        }

        logger.debug("Edge options configured");

        try {
            return new EdgeDriver(options);
        } catch (Exception e) {
            logger.error("Failed to initialize EdgeDriver", e);
            throw e;
        }
    }

    /**
     * Add common options to all browsers
     * These options improve stability and compatibility
     *
     * @param options the options object (ChromeOptions, FirefoxOptions, or EdgeOptions)
     */
    private static void addCommonOptions(Object options) {
        logger.debug("Adding common options to browser");

        // All browsers should use these options
        if (options instanceof ChromeOptions) {
            ChromeOptions chromeOptions = (ChromeOptions) options;
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--disable-extensions");
            chromeOptions.addArguments("--disable-gpu");
            chromeOptions.addArguments("--disable-web-security");
            chromeOptions.addArguments("--allow-running-insecure-content");
        } else if (options instanceof FirefoxOptions) {
            FirefoxOptions firefoxOptions = (FirefoxOptions) options;
            firefoxOptions.addArguments("--no-sandbox");
        } else if (options instanceof EdgeOptions) {
            EdgeOptions edgeOptions = (EdgeOptions) options;
            edgeOptions.addArguments("--no-sandbox");
            edgeOptions.addArguments("--disable-dev-shm-usage");
            edgeOptions.addArguments("--disable-extensions");
            edgeOptions.addArguments("--disable-gpu");
            edgeOptions.addArguments("--disable-web-security");
            edgeOptions.addArguments("--allow-running-insecure-content");
        }
    }
}

