package org.epam.tests;

import io.qameta.allure.*;
import org.epam.base.BaseTest;
import org.epam.config.ConfigManager;
import org.epam.exceptions.ElementNotFoundException;
import org.epam.exceptions.NavigationException;
import org.epam.exceptions.TimeoutException;
import org.epam.pages.LoginPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Application")
@Feature("User Authentication")
public class LoginTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);

    @Test(priority = 1, description = "Verify successful login with valid credentials")
    @Story("Valid Login")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify that user can login successfully with valid credentials")
    public void testValidLogin() {
        try {
            logStep("Starting valid login test");
            logger.info("=== TEST: testValidLogin ===");

            // Create page object
            LoginPage loginPage = new LoginPage();

            // Navigate to login page
            logStep("Navigating to SauceDemo website");
            try {
                Allure.step("Navigate to SauceDemo login page", () -> {
                    loginPage.navigateToLoginPage();
                });
            } catch (NavigationException e) {
                logger.error("Navigation failed: {}", e.getMessage());
                Assert.fail("Failed to navigate to login page: " + e.getMessage());
            }

            // Verify login page is displayed
            logStep("Verifying login page is displayed");
            Allure.step("Verify login page is displayed", () -> {
                try {
                    Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
                } catch (AssertionError e) {
                    logger.error("Login page not displayed: {}", e.getMessage());
                    throw e;
                }
            });

            // Perform login
            logStep("Entering valid credentials and logging in");
            try {
                Allure.step("Login with valid credentials", () -> {
                    loginPage.login("standard_user", "secret_sauce");
                });
            } catch (ElementNotFoundException e) {
                logger.error("Login element not found: {}", e.getMessage());
                Assert.fail("Login failed - element not found: " + e.getMessage());
            }

            // Verify successful login (URL should change)
            logStep("Verifying successful login");
            Allure.step("Verify successful login", () -> {
                try {
                    String currentUrl = loginPage.getCurrentUrl();
                    Assert.assertTrue(currentUrl.contains("inventory"), "Should be redirected to inventory page after login");
                    logger.info("Login successful - redirected to inventory page: {}", currentUrl);
                } catch (AssertionError e) {
                    logger.error("Login verification failed: {}", e.getMessage());
                    throw e;
                }
            });

            logStep("Valid login test completed successfully");
            logger.info("=== TEST PASSED: testValidLogin ===");
        } catch (ElementNotFoundException e) {
            logger.error("Test failed - element not found: {}", e.getMessage());
            throw new AssertionError("Test failed: " + e.getMessage(), e);
        } catch (NavigationException e) {
            logger.error("Test failed - navigation error: {}", e.getMessage());
            throw new AssertionError("Test failed: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Test failed with unexpected exception: {}", e.getMessage(), e);
            throw new AssertionError("Test failed: " + e.getMessage(), e);
        }
    }

    @Test(priority = 2, description = "Verify login fails with invalid credentials")
    @Story("Invalid Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify that login fails with invalid credentials and shows appropriate error message")
    public void testInvalidLogin() {
        try {
            logStep("Starting invalid login test");
            logger.info("=== TEST: testInvalidLogin ===");

            // Create page object
            LoginPage loginPage = new LoginPage();

            // Navigate to login page
            logStep("Navigating to SauceDemo website");
            try {
                Allure.step("Navigate to SauceDemo login page", () -> {
                    loginPage.navigateToLoginPage();
                });
            } catch (NavigationException e) {
                logger.error("Navigation failed: {}", e.getMessage());
                Assert.fail("Failed to navigate to login page: " + e.getMessage());
            }

            // Verify login page is displayed
            logStep("Verifying login page is displayed");
            Allure.step("Verify login page is displayed", () -> {
                try {
                    Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
                } catch (AssertionError e) {
                    logger.error("Login page not displayed: {}", e.getMessage());
                    throw e;
                }
            });

            // Perform login with invalid credentials
            logStep("Entering invalid credentials");
            try {
                Allure.step("Login with invalid credentials", () -> {
                    loginPage.login("invalid_user", "wrong_password");
                });
            } catch (ElementNotFoundException e) {
                logger.error("Login element not found: {}", e.getMessage());
                Assert.fail("Login failed - element not found: " + e.getMessage());
            }

            // Verify error message is displayed
            logStep("Verifying error message is displayed");
            Allure.step("Verify error message is displayed", () -> {
                try {
                    Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
                    String errorMessage = loginPage.getErrorMessage();
                    Assert.assertTrue(errorMessage.contains("Username and password do not match"),
                            "Error message should indicate invalid credentials");
                    logger.info("Error message verified: {}", errorMessage);
                } catch (ElementNotFoundException e) {
                    logger.error("Error message element not found: {}", e.getMessage());
                    Assert.fail("Error message not found: " + e.getMessage());
                } catch (AssertionError e) {
                    logger.error("Error message assertion failed: {}", e.getMessage());
                    throw e;
                }
            });

            logStep("Invalid login test completed successfully");
            logger.info("=== TEST PASSED: testInvalidLogin ===");
        } catch (Exception e) {
            logger.error("Test failed with exception: {}", e.getMessage(), e);
            throw new AssertionError("Test failed: " + e.getMessage(), e);
        }
    }

    @Test(priority = 3, description = "Verify login fails with locked user")
    @Story("Locked User Login")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify that locked user cannot login and shows appropriate error message")
    public void testLockedUserLogin() {
        try {
            logStep("Starting locked user login test");
            logger.info("=== TEST: testLockedUserLogin ===");

            // Create page object
            LoginPage loginPage = new LoginPage();

            // Navigate to login page
            logStep("Navigating to SauceDemo website");
            try {
                Allure.step("Navigate to SauceDemo login page", () -> {
                    loginPage.navigateToLoginPage();
                });
            } catch (NavigationException e) {
                logger.error("Navigation failed: {}", e.getMessage());
                Assert.fail("Failed to navigate to login page: " + e.getMessage());
            }

            // Perform login with locked user
            logStep("Entering locked user credentials");
            try {
                Allure.step("Login with locked user credentials", () -> {
                    loginPage.login("locked_out_user", "secret_sauce");
                });
            } catch (ElementNotFoundException e) {
                logger.error("Login element not found: {}", e.getMessage());
                Assert.fail("Login failed - element not found: " + e.getMessage());
            }

            // Verify error message for locked user
            logStep("Verifying locked user error message");
            Allure.step("Verify locked user error message", () -> {
                try {
                    Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for locked user");
                    String errorMessage = loginPage.getErrorMessage();
                    Assert.assertTrue(errorMessage.contains("locked out"),
                            "Error message should indicate user is locked out");
                    logger.info("Locked user error verified: {}", errorMessage);
                } catch (ElementNotFoundException e) {
                    logger.error("Error message element not found: {}", e.getMessage());
                    Assert.fail("Error message not found: " + e.getMessage());
                } catch (AssertionError e) {
                    logger.error("Locked user assertion failed: {}", e.getMessage());
                    throw e;
                }
            });

            logStep("Locked user login test completed successfully");
            logger.info("=== TEST PASSED: testLockedUserLogin ===");
        } catch (Exception e) {
            logger.error("Test failed with exception: {}", e.getMessage(), e);
            throw new AssertionError("Test failed: " + e.getMessage(), e);
        }
    }
}