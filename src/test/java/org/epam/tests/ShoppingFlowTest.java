package org.epam.tests;

import io.qameta.allure.*;
import org.epam.base.BaseTest;
import org.epam.exceptions.ElementNotFoundException;
import org.epam.exceptions.NavigationException;
import org.epam.pages.LoginPage;
import org.epam.pages.ProductsPage;
import org.epam.pages.CartPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("SauceDemo Application")
@Feature("Shopping Flow")
public class ShoppingFlowTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingFlowTest.class);

    @Test(priority = 1, description = "Complete shopping flow - Add product to cart")
    @Story("Add Single Product")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddProductToCart() {
        try {
            logStep("Starting add product to cart test");
            logger.info("=== TEST: testAddProductToCart ===");

            // Step 1: Login
            LoginPage loginPage = new LoginPage();
            logStep("Navigating to SauceDemo website");
            try {
                Allure.step("Navigate to SauceDemo", () -> {
                    loginPage.navigateToLoginPage();
                });
            } catch (NavigationException e) {
                logger.error("Navigation failed: {}", e.getMessage());
                Assert.fail("Failed to navigate: " + e.getMessage());
            }

            logStep("Logging in with valid credentials");
            try {
                Allure.step("Login with standard user", () -> {
                    loginPage.login("standard_user", "secret_sauce");
                });
            } catch (ElementNotFoundException e) {
                logger.error("Login element not found: {}", e.getMessage());
                Assert.fail("Login failed: " + e.getMessage());
            }

            // Step 2: Verify we're on products page
            ProductsPage productsPage = new ProductsPage();
            logStep("Verifying products page is displayed");
            try {
                Allure.step("Verify products page", () -> {
                    Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Should be on products page after login");
                });
            } catch (AssertionError e) {
                logger.error("Products page not displayed: {}", e.getMessage());
                throw e;
            }

            // Step 3: Add product to cart
            logStep("Adding backpack to cart");
            try {
                Allure.step("Add backpack to cart", () -> {
                    productsPage.addBackpackToCart();
                });
            } catch (ElementNotFoundException e) {
                logger.error("Failed to add product: {}", e.getMessage());
                Assert.fail("Cannot add product: " + e.getMessage());
            }

            // Step 4: Verify cart badge shows 1 item
            logStep("Verifying cart shows 1 item");
            try {
                Allure.step("Verify cart count", () -> {
                    String cartCount = productsPage.getCartItemCount();
                    Assert.assertEquals(cartCount, "1", "Cart should show 1 item");
                    logger.info("Cart count verified: {}", cartCount);
                });
            } catch (AssertionError e) {
                logger.error("Cart count assertion failed: {}", e.getMessage());
                throw e;
            }

            // Step 5: Go to cart page
            logStep("Navigating to cart page");
            try {
                Allure.step("Click shopping cart", () -> {
                    productsPage.clickShoppingCart();
                });
            } catch (ElementNotFoundException e) {
                logger.error("Cart button not found: {}", e.getMessage());
                Assert.fail("Cannot navigate to cart: " + e.getMessage());
            }

            // Step 6: Verify cart page and item
            CartPage cartPage = new CartPage();
            logStep("Verifying cart page is displayed");
            try {
                Allure.step("Verify cart page", () -> {
                    Assert.assertTrue(cartPage.isCartPageDisplayed(), "Should be on cart page");
                });
            } catch (AssertionError e) {
                logger.error("Cart page not displayed: {}", e.getMessage());
                throw e;
            }

            logStep("Verifying cart contains 1 item");
            try {
                Allure.step("Verify cart item count", () -> {
                    Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart should contain 1 item");
                });
            } catch (AssertionError e) {
                logger.error("Cart item count assertion failed: {}", e.getMessage());
                throw e;
            }

            logStep("Verifying correct item is in cart");
            try {
                Allure.step("Verify backpack in cart", () -> {
                    Assert.assertTrue(cartPage.isItemInCart("Backpack"), "Cart should contain the backpack");
                    logger.info("Backpack verified in cart");
                });
            } catch (AssertionError e) {
                logger.error("Item verification failed: {}", e.getMessage());
                throw e;
            }

            logStep("Add product to cart test completed successfully");
            logger.info("=== TEST PASSED: testAddProductToCart ===");
        } catch (Exception e) {
            logger.error("Test failed with exception: {}", e.getMessage(), e);
            throw new AssertionError("Test failed: " + e.getMessage(), e);
        }
    }
    
    // Other test methods disabled - keeping only testAddProductToCart for 4-test total
}