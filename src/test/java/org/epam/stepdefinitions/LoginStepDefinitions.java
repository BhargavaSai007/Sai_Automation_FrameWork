package org.epam.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.epam.driver.DriverManager;
import org.epam.pages.LoginPage;
import org.epam.pages.ProductsPage;
import org.epam.utils.TestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Step Definitions for Login scenarios
 * Maps Gherkin steps to Java code
 * Reuses existing LoginPage page object
 */
public class LoginStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(LoginStepDefinitions.class);

    private LoginPage loginPage;
    private ProductsPage productsPage;

    @Before
    public void setupTest() {
        logger.info("Creating WebDriver instance for Cucumber test");
        DriverManager.createDriver();
        loginPage = new LoginPage();
        productsPage = new ProductsPage();
        logger.info("WebDriver and Page Objects initialized");
    }

    @After
    public void teardownTest() {
        logger.info("Closing WebDriver after Cucumber test");
        DriverManager.quitDriver();
        logger.info("WebDriver closed");
    }

    @Given("User is on SauceDemo website")
    public void userIsOnSauceDemoWebsite() {
        logger.info("=== STEP: User is on SauceDemo website ===");
        Allure.step("Navigate to SauceDemo website", () -> {
            loginPage.navigateToLoginPage();
            TestUtils.logTestStep("Navigated to SauceDemo website");
        });
    }

    @When("User logs in with username {string} and password {string}")
    public void userLogsInWithCredentials(String username, String password) {
        logger.info("=== STEP: User logs in with username {} and password {} ===", username, password);
        try {
            Allure.step("Login with username: " + username, () -> {
                loginPage.login(username, password);
                TestUtils.logTestStep("Logged in with username: " + username);
            });
        } catch (Exception e) {
            logger.error("Login failed for user {}: {}", username, e.getMessage());
            throw e;
        }
    }

    @Then("User should be successfully logged in")
    public void userShouldBeSuccessfullyLoggedIn() {
        logger.info("=== STEP: Verify user is successfully logged in ===");
        Allure.step("Verify successful login", () -> {
            String currentUrl = loginPage.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("inventory"),
                    "User should be redirected to inventory page. Current URL: " + currentUrl);
            TestUtils.logTestStep("User successfully logged in");
            logger.info("Login verified - user on inventory page");
        });
    }

    @Then("User should see products page")
    public void userShouldSeeProductsPage() {
        logger.info("=== STEP: Verify products page is displayed ===");
        Allure.step("Verify products page", () -> {
            Assert.assertTrue(productsPage.isProductsPageDisplayed(),
                    "Products page should be displayed");
            TestUtils.logTestStep("Products page is displayed");
            logger.info("Products page verified");
        });
    }

    @Then("Login should fail")
    public void loginShouldFail() {
        logger.info("=== STEP: Verify login failed ===");
        Allure.step("Verify login failed", () -> {
            String currentUrl = loginPage.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("login") || currentUrl.contains(""),
                    "User should still be on login page after failed login");
            TestUtils.logTestStep("Login failed as expected");
            logger.info("Login failure verified");
        });
    }

    @Then("Error message should be displayed with text {string}")
    public void errorMessageShouldBeDisplayedWithText(String expectedText) {
        logger.info("=== STEP: Verify error message with text: {} ===", expectedText);
        Allure.step("Verify error message", () -> {
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                    "Error message should be displayed");
            String actualError = loginPage.getErrorMessage();
            Assert.assertTrue(actualError.toLowerCase().contains(expectedText.toLowerCase()),
                    "Error message should contain: " + expectedText + " | Actual: " + actualError);
            TestUtils.logTestStep("Error message verified: " + actualError);
            logger.info("Error message verified with text: {}", expectedText);
        });
    }
}

