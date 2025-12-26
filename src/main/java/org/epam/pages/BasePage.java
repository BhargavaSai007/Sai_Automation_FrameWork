package org.epam.pages;

import org.epam.driver.DriverManager;
import org.epam.exceptions.ElementNotFoundException;
import org.epam.exceptions.TimeoutException;
import org.epam.exceptions.NavigationException;
import org.epam.utils.RetryUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.epam.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * BasePage - Base class for all Page Objects
 *
 * Features:
 * - Common element operations (click, type, wait, etc.)
 * - Retry mechanism with 3 attempts
 * - Custom exception throwing
 * - Full logging of all operations
 * - Thread-safe WebDriver access
 *
 * Design Patterns Used:
 * - Page Object Model: Encapsulates page elements and actions
 * - Template Method: Common methods reused by all pages
 * - Retry Pattern: Automatic retry for flaky operations
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);

    private static final int MAX_RETRIES = 3;

    /**
     * Constructor - Initializes driver and wait
     * Called by all page objects (LoginPage, ProductsPage, etc.)
     */
    public BasePage() {
        logger.debug("Initializing BasePage");
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigManager.getTimeout()));
        logger.debug("BasePage initialized with timeout: {} seconds", ConfigManager.getTimeout());
    }

    /**
     * Wait for element to be visible (with retry)
     *
     * Retry Logic:
     * - Attempt 1: Find element → Not found
     * - Attempt 2: Wait 1 sec, find → Not found
     * - Attempt 3: Wait 2 sec, find → Found OR throw exception
     *
     * @param locator element locator
     * @return visible WebElement
     * @throws ElementNotFoundException if not found after 3 attempts
     */
    protected WebElement waitForElementToBeVisible(By locator) {
        logger.debug("Waiting for element to be visible: {}", locator);

        try {
            WebElement element = RetryUtils.retryOperation(
                () -> {
                    try {
                        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                    } catch (org.openqa.selenium.TimeoutException e) {
                        throw new TimeoutException("Element not visible within timeout: " + locator, e);
                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        throw new ElementNotFoundException("Element not found in DOM: " + locator, e);
                    }
                },
                "Element to be visible: " + locator,
                3  // MAX_RETRIES
            );
            logger.debug("Element is now visible: {}", locator);
            return element;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for visible element: {}", locator);
            throw e;
        } catch (ElementNotFoundException e) {
            logger.error("Element not found while waiting for visibility: {}", locator);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while waiting for visibility: {} | Error: {}", locator, e.getMessage(), e);
            throw new ElementNotFoundException("Error waiting for element visibility: " + locator, e);
        }
    }

    protected void waitForUrlContains(String urlFragment) {
        logger.info("Waiting for URL to contain: {}", urlFragment);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains(urlFragment));
        logger.info("URL contains: {}", urlFragment);
    }

    /**
     * Wait for element to be clickable (with retry)
     *
     * @param locator element locator
     * @return clickable WebElement
     * @throws ElementNotFoundException if not clickable after 3 attempts
     */
    protected WebElement waitForElementToBeClickable(By locator) {
        logger.debug("Waiting for element to be clickable: {}", locator);

        try {
            WebElement element = RetryUtils.retryOperation(
                () -> {
                    try {
                        return wait.until(ExpectedConditions.elementToBeClickable(locator));
                    } catch (org.openqa.selenium.TimeoutException e) {
                        throw new TimeoutException("Element not clickable within timeout: " + locator, e);
                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        throw new ElementNotFoundException("Element not found in DOM: " + locator, e);
                    }
                },
                "Element to be clickable: " + locator,
                3  // MAX_RETRIES
            );
            logger.debug("Element is now clickable: {}", locator);
            return element;
        } catch (TimeoutException e) {
            logger.error("Timeout waiting for clickable element: {}", locator);
            throw e;
        } catch (ElementNotFoundException e) {
            logger.error("Element not found while waiting for clickability: {}", locator);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while waiting for clickability: {} | Error: {}", locator, e.getMessage(), e);
            throw new ElementNotFoundException("Error waiting for element clickability: " + locator, e);
        }
    }

    /**
     * Click element safely (with retry)
     *
     * Retry will happen automatically if:
     * - Element not found
     * - Element not clickable
     * - Element found but clicking fails
     *
     * @param locator element locator
     * @throws ElementNotFoundException if cannot click after 3 attempts
     */
    protected void clickElement(By locator) {
        logger.debug("Attempting to click element: {}", locator);

        try {
            RetryUtils.retryOperation(
                () -> {
                    try {
                        WebElement element = waitForElementToBeClickable(locator);
                        element.click();
                        logger.info("Element clicked successfully: {}", locator);
                        return null;  // void operation, return null
                    } catch (org.openqa.selenium.StaleElementReferenceException e) {
                        logger.warn("Stale element reference while clicking: {}", locator);
                        throw new ElementNotFoundException("Stale element (DOM changed) while clicking: " + locator, e);
                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        logger.warn("Element not found while clicking: {}", locator);
                        throw new ElementNotFoundException("Element not found for click: " + locator, e);
                    } catch (org.openqa.selenium.ElementNotInteractableException e) {
                        logger.warn("Element not interactable while clicking: {}", locator);
                        throw new ElementNotFoundException("Element not interactable (may be hidden): " + locator, e);
                    } catch (org.openqa.selenium.TimeoutException e) {
                        logger.warn("Timeout while waiting for element to be clickable: {}", locator);
                        throw new TimeoutException("Timeout waiting for clickable element: " + locator, e);
                    }
                },
                "Click element: " + locator,
                3  // MAX_RETRIES
            );
        } catch (ElementNotFoundException e) {
            logger.error("Failed to click element after retries: {} | Error: {}", locator, e.getMessage());
            throw e;
        } catch (TimeoutException e) {
            logger.error("Timeout while clicking element: {} | Error: {}", locator, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while clicking element: {} | Error: {}", locator, e.getMessage(), e);
            throw new ElementNotFoundException("Cannot click element - unexpected error: " + locator, e);
        }
    }

    /**
     * Type text into element safely (with retry)
     *
     * Steps:
     * 1. Wait for element to be visible
     * 2. Clear existing text
     * 3. Type new text
     *
     * Each step is retried up to 3 times
     *
     * @param locator element locator
     * @param text text to type
     * @throws ElementNotFoundException if cannot type after 3 attempts
     */
    protected void typeText(By locator, String text) {
        logger.debug("Attempting to type text in element: {}", locator);

        try {
            if (text == null || text.isEmpty()) {
                logger.warn("Text to type is null or empty for locator: {}", locator);
            }

            RetryUtils.retryOperation(
                () -> {
                    try {
                        WebElement element = waitForElementToBeVisible(locator);

                        try {
                            element.clear();
                            logger.debug("Element cleared for typing");
                        } catch (Exception e) {
                            logger.warn("Error clearing element before typing: {}", e.getMessage());
                            // Don't fail on clear - try to type anyway
                        }

                        element.sendKeys(text);
                        logger.info("Text typed successfully into element: {} | Text length: {}", locator, text.length());
                        return null;
                    } catch (org.openqa.selenium.StaleElementReferenceException e) {
                        logger.warn("Stale element reference while typing: {}", locator);
                        throw new ElementNotFoundException("Stale element (DOM changed) while typing: " + locator, e);
                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        logger.warn("Element not found while typing: {}", locator);
                        throw new ElementNotFoundException("Element not found for text input: " + locator, e);
                    } catch (org.openqa.selenium.ElementNotInteractableException e) {
                        logger.warn("Element not interactable while typing: {}", locator);
                        throw new ElementNotFoundException("Element not interactable (may be hidden): " + locator, e);
                    } catch (org.openqa.selenium.TimeoutException e) {
                        logger.warn("Timeout while waiting for element to be visible: {}", locator);
                        throw new TimeoutException("Timeout waiting for visible element: " + locator, e);
                    }
                },
                "Type text in element: " + locator,
                3  // MAX_RETRIES
            );
        } catch (ElementNotFoundException e) {
            logger.error("Failed to type text after retries: {} | Error: {}", locator, e.getMessage());
            throw e;
        } catch (TimeoutException e) {
            logger.error("Timeout while typing in element: {} | Error: {}", locator, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while typing: {} | Error: {}", locator, e.getMessage(), e);
            throw new ElementNotFoundException("Cannot type text - unexpected error: " + locator, e);
        }
    }

    /**
     * Get text from element (with retry)
     *
     * @param locator element locator
     * @return element text
     * @throws ElementNotFoundException if cannot get text after 3 attempts
     */
    protected String getElementText(By locator) {
        logger.debug("Attempting to get text from element: {}", locator);

        try {
            String text = RetryUtils.retryOperation(
                () -> {
                    try {
                        String retrievedText = waitForElementToBeVisible(locator).getText();
                        logger.info("Text retrieved from element: {} = '{}'", locator, retrievedText);
                        return retrievedText;
                    } catch (org.openqa.selenium.StaleElementReferenceException e) {
                        logger.warn("Stale element reference while getting text: {}", locator);
                        throw new ElementNotFoundException("Stale element (DOM changed): " + locator, e);
                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        logger.warn("Element not found while getting text: {}", locator);
                        throw new ElementNotFoundException("Element not found for text retrieval: " + locator, e);
                    } catch (org.openqa.selenium.TimeoutException e) {
                        logger.warn("Timeout while waiting for element: {}", locator);
                        throw new TimeoutException("Timeout waiting for element: " + locator, e);
                    }
                },
                "Get text from element: " + locator,
                3  // MAX_RETRIES
            );
            return text;
        } catch (ElementNotFoundException e) {
            logger.error("Failed to get text after retries: {} | Error: {}", locator, e.getMessage());
            throw e;
        } catch (TimeoutException e) {
            logger.error("Timeout while getting text from element: {} | Error: {}", locator, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while getting text: {} | Error: {}", locator, e.getMessage(), e);
            throw new ElementNotFoundException("Cannot get text - unexpected error: " + locator, e);
        }
    }

    /**
     * Check if element is displayed (no retry - quick check)
     *
     * @param locator element locator
     * @return true if element is visible, false otherwise
     */
    protected boolean isElementDisplayed(By locator) {
        logger.debug("Checking if element is displayed: {}", locator);
        try {
            try {
                boolean isDisplayed = waitForElementToBeVisible(locator).isDisplayed();
                logger.debug("Element display status: {}", isDisplayed);
                return isDisplayed;
            } catch (TimeoutException e) {
                logger.debug("Element not visible (timeout): {}", locator);
                return false;
            } catch (ElementNotFoundException e) {
                logger.debug("Element not found for display check: {}", locator);
                return false;
            }
        } catch (Exception e) {
            logger.debug("Error checking element display status: {} | Error: {}", locator, e.getMessage());
            return false;
        }
    }

    /**
     * Navigate to URL (with exception handling)
     *
     * @param url the URL to navigate to
     * @throws NavigationException if navigation fails
     */
    public void navigateToUrl(String url) {
        logger.info("Navigating to URL: {}", url);
        try {
            if (url == null || url.trim().isEmpty()) {
                throw new NavigationException("URL is null or empty");
            }

            try {
                driver.navigate().to(url);
                logger.info("Successfully navigated to: {}", url);

                // Wait for page to load
                try {
                    waitForPageLoad();
                } catch (Exception e) {
                    logger.warn("Page load wait timed out, but continuing: {}", url);
                }
            } catch (org.openqa.selenium.UnhandledAlertException e) {
                logger.error("Unhandled alert while navigating to: {}", url);
                throw new NavigationException("Unhandled alert during navigation: " + url, e);
            } catch (org.openqa.selenium.TimeoutException e) {
                logger.error("Timeout while navigating to: {}", url);
                throw new TimeoutException("Navigation timeout: " + url, e);
            }
        } catch (NavigationException e) {
            logger.error("Navigation exception: {}", e.getMessage());
            throw e;
        } catch (TimeoutException e) {
            logger.error("Timeout during navigation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while navigating to: {} | Error: {}", url, e.getMessage(), e);
            throw new NavigationException("Cannot navigate to URL: " + url, e);
        }
    }

    /**
     * Get current page title
     * @return page title
     */
    public String getPageTitle() {
        String title = driver.getTitle();
        logger.debug("Current page title: {}", title);
        return title;
    }

    /**
     * Get current URL
     * @return current page URL
     */
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        logger.debug("Current page URL: {}", url);
        return url;
    }

    /**
     * Wait for page to load completely
     * Useful before performing actions
     */
    public void waitForPageLoad() {
        logger.debug("Waiting for page to load");
        try {
            wait.until(webDriver -> {
                try {
                    boolean isReady = (Boolean) ((org.openqa.selenium.JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState").equals("complete");
                    logger.debug("Page ready state: {}", isReady);
                    return isReady;
                } catch (Exception e) {
                    logger.debug("Error checking page readyState: {}", e.getMessage());
                    return false;
                }
            });
            logger.info("Page loaded successfully");
        } catch (org.openqa.selenium.TimeoutException e) {
            logger.warn("Page load wait timed out (continuing anyway): {}", e.getMessage());
            throw new TimeoutException("Page load timeout", e);
        } catch (Exception e) {
            logger.warn("Error waiting for page load: {} (continuing anyway)", e.getMessage());
        }
    }
}