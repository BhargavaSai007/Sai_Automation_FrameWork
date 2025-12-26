package org.epam.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.qameta.allure.Allure;
import org.epam.pages.CartPage;
import org.epam.pages.ProductsPage;
import org.epam.utils.TestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Step Definitions for Shopping scenarios
 * Maps Gherkin steps for shopping functionality to Java code
 * Reuses existing ProductsPage and CartPage page objects
 */
public class ShoppingStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingStepDefinitions.class);

    private ProductsPage productsPage;
    private CartPage cartPage;

    // Constructor to reuse page objects from LoginStepDefinitions
    public ShoppingStepDefinitions() {
        this.productsPage = new ProductsPage();
        this.cartPage = new CartPage();
    }

    @And("User is on products page")
    public void userIsOnProductsPage() {
        logger.info("=== STEP: Verify user is on products page ===");
        Allure.step("Verify on products page", () -> {
            Assert.assertTrue(productsPage.isProductsPageDisplayed(),
                    "User should be on products page");
            TestUtils.logTestStep("User is on products page");
            logger.info("Products page verified");
        });
    }

    @And("User adds {string} product to cart")
    public void userAddsProductToCart(String productName) {
        logger.info("=== STEP: User adds {} product to cart ===", productName);
        try {
            Allure.step("Add " + productName + " to cart", () -> {
                if (productName.toLowerCase().contains("backpack")) {
                    productsPage.addBackpackToCart();
                } else if (productName.toLowerCase().contains("bike light")) {
                    productsPage.addBikeLightToCart();
                } else if (productName.toLowerCase().contains("t-shirt")) {
                    productsPage.addTshirtToCart();
                } else {
                    throw new IllegalArgumentException("Unknown product: " + productName);
                }
                TestUtils.logTestStep("Product added to cart: " + productName);
                logger.info("Product {} added to cart", productName);
            });
        } catch (Exception e) {
            logger.error("Failed to add product {} to cart: {}", productName, e.getMessage());
            throw e;
        }
    }

    @Then("Cart should show {int} item")
    public void cartShouldShowItem(int expectedCount) {
        logger.info("=== STEP: Verify cart shows {} item(s) ===", expectedCount);
        Allure.step("Verify cart item count: " + expectedCount, () -> {
            String cartCount = productsPage.getCartItemCount();
            Assert.assertEquals(cartCount, String.valueOf(expectedCount),
                    "Cart should show " + expectedCount + " item(s)");
            TestUtils.logTestStep("Cart shows " + expectedCount + " item(s)");
            logger.info("Cart item count verified: {}", expectedCount);
        });
    }

    @When("User navigates to shopping cart")
    public void userNavigatesToShoppingCart() {
        logger.info("=== STEP: User navigates to shopping cart ===");
        try {
            Allure.step("Navigate to shopping cart", () -> {
                productsPage.clickShoppingCart();
                TestUtils.logTestStep("Navigated to shopping cart");
                logger.info("User navigated to shopping cart");
            });
        } catch (Exception e) {
            logger.error("Failed to navigate to shopping cart: {}", e.getMessage());
            throw e;
        }
    }

    @Then("Cart page should display")
    public void cartPageShouldDisplay() {
        logger.info("=== STEP: Verify cart page is displayed ===");
        Allure.step("Verify cart page display", () -> {
            Assert.assertTrue(cartPage.isCartPageDisplayed(),
                    "Cart page should be displayed");
            TestUtils.logTestStep("Cart page is displayed");
            logger.info("Cart page verified");
        });
    }

    @And("Cart should contain {string} product")
    public void cartShouldContainProduct(String productName) {
        logger.info("=== STEP: Verify cart contains {} product ===", productName);
        Allure.step("Verify " + productName + " in cart", () -> {
            Assert.assertTrue(cartPage.isItemInCart(productName),
                    "Cart should contain " + productName + " product");
            TestUtils.logTestStep("Cart contains " + productName);
            logger.info("Product {} verified in cart", productName);
        });
    }
}

