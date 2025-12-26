package org.epam.pages;

import org.epam.config.ConfigManager;
import org.epam.exceptions.ElementNotFoundException;
import org.epam.exceptions.NavigationException;
import org.epam.exceptions.TimeoutException;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Login Page Object
 *
 * Encapsulates all login page elements and actions
 * Extends BasePage for retry mechanism and common operations
 *
 * Features:
 * - Login with username and password
 * - Verify login success/failure
 * - Validate error messages
 * - Check page display status
 */
public class LoginPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    // Page URLs
    private static final String LOGIN_PAGE_URL = "https://www.saucedemo.com";

    // Page elements (locators)
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");
    private final By errorButton = By.cssSelector("[data-test='error-button']");
    private final By logoImage = By.className("login_logo");

    /**
     * Enter username into username field
     *
     * @param username the username to enter
     * @throws ElementNotFoundException if field not found
     */
    public void enterUsername(String username) {
        logger.info("Entering username: {}", username);
        typeText(usernameField, username);
        logger.debug("Username entered successfully");
    }

    /**
     * Enter password into password field
     *
     * @param password the password to enter
     * @throws ElementNotFoundException if field not found
     */
    public void enterPassword(String password) {
        logger.info("Entering password");
        typeText(passwordField, password);
        logger.debug("Password entered successfully");
    }

    /**
     * Click the login button
     *
     * @throws ElementNotFoundException if button not found or not clickable
     */
    public void clickLoginButton() {
        logger.info("Clicking login button");
        clickElement(loginButton);
        logger.debug("Login button clicked");
    }

    /**
     * Complete login flow: enter credentials and click login
     *
     * @param username the username
     * @param password the password
     * @throws ElementNotFoundException if any element not found
     */
    public void login(String username, String password) {
        logger.info("Starting login flow with username: {}", username);
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new ElementNotFoundException("Username cannot be null or empty");
            }
            if (password == null || password.isEmpty()) {
                throw new ElementNotFoundException("Password cannot be null or empty");
            }

            try {
                enterUsername(username);
                enterPassword(password);
                clickLoginButton();
                logger.info("Login flow completed successfully");
            } catch (ElementNotFoundException e) {
                logger.error("Login failed - element not found: {}", e.getMessage());
                throw e;
            } catch (Exception e) {
                logger.error("Login failed with unexpected error: {}", e.getMessage(), e);
                throw new ElementNotFoundException("Login operation failed: " + e.getMessage(), e);
            }
        } catch (ElementNotFoundException e) {
            throw e;
        }
    }

    /**
     * Verify if login page is displayed
     *
     * @return true if login logo visible, false otherwise
     */
    public boolean isLoginPageDisplayed() {
        logger.debug("Checking if login page is displayed");
        boolean isDisplayed = isElementDisplayed(logoImage);
        logger.info("Login page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Get error message text
     *
     * @return error message text
     * @throws ElementNotFoundException if error message not found
     */
    public String getErrorMessage() {
        logger.debug("Retrieving error message");
        try {
            String errorText = getElementText(errorMessage);
            logger.info("Error message retrieved: {}", errorText);
            return errorText;
        } catch (ElementNotFoundException e) {
            logger.error("Error message not found: {}", e.getMessage());
            throw e;
        } catch (TimeoutException e) {
            logger.error("Timeout while retrieving error message: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving error message: {}", e.getMessage(), e);
            throw new ElementNotFoundException("Cannot retrieve error message: " + e.getMessage(), e);
        }
    }

    /**
     * Check if error message is displayed
     *
     * @return true if error message visible, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        logger.debug("Checking if error message is displayed");
        boolean isDisplayed = isElementDisplayed(errorMessage);
        logger.info("Error message displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Clear error message by clicking error button
     * Useful for dismissing error and trying again
     *
     * @throws ElementNotFoundException if error button not found
     */
    public void clearErrorMessage() {
        logger.debug("Attempting to clear error message");
        try {
            if (isErrorMessageDisplayed()) {
                try {
                    clickElement(errorButton);
                    logger.info("Error message cleared successfully");
                } catch (ElementNotFoundException e) {
                    logger.warn("Could not click error clear button: {}", e.getMessage());
                    throw e;
                }
            } else {
                logger.debug("No error message to clear");
            }
        } catch (ElementNotFoundException e) {
            logger.warn("Error clear operation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while clearing error message: {}", e.getMessage(), e);
            throw new ElementNotFoundException("Cannot clear error message: " + e.getMessage(), e);
        }
    }

    /**
     * Navigate to login page
     * Uses base URL from config
     * @throws NavigationException if navigation fails
     */
    public void navigateToLoginPage() {
        logger.info("Navigating to login page: {}", LOGIN_PAGE_URL);
        try {
            driver.get(LOGIN_PAGE_URL);
            logger.info("Navigated to login page successfully");
            waitForPageLoad();
        } catch (Exception e) {
            logger.error("Failed to navigate to login page: {}", e.getMessage(), e);
            throw new NavigationException("Cannot navigate to login page: " + LOGIN_PAGE_URL, e);
        }
    }

    /**
     * Navigate to login page using configured base URL
     * @throws NavigationException if navigation fails
     */
    public void navigateToBaseUrl() {
        String baseUrl = ConfigManager.getBaseUrl();
        logger.info("Navigating to base URL: {}", baseUrl);
        try {
            if (baseUrl == null || baseUrl.trim().isEmpty()) {
                throw new NavigationException("Base URL is null or empty in configuration");
            }
            driver.get(baseUrl);
            logger.info("Navigated to base URL successfully");
            waitForPageLoad();
        } catch (NavigationException e) {
            logger.error("Navigation exception: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Failed to navigate to base URL: {}", e.getMessage(), e);
            throw new NavigationException("Cannot navigate to base URL: " + baseUrl, e);
        }
    }

    /**
     * Get current page URL
     *
     * @return current URL
     */
    public String getCurrentPageUrl() {
        String url = getCurrentUrl();
        logger.debug("Current page URL: {}", url);
        return url;
    }

    /**
     * Verify login was successful by checking URL
     *
     * @return true if URL contains "inventory", false otherwise
     */
    public boolean isLoginSuccessful() {
        logger.debug("Verifying login success");
        boolean isSuccessful = getCurrentUrl().contains("inventory");
        logger.info("Login successful: {}", isSuccessful);
        return isSuccessful;
    }
}