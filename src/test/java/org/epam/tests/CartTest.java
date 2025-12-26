package org.epam.tests;

import org.epam.base.BaseTest;
import org.epam.pages.LoginPage;
import org.epam.pages.ProductsPage;
import org.epam.pages.CartPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test(priority = 1, description = "Verify user can add products to cart and view them in cart page")
    public void testAddProductsToCartAndViewCart() {
        logStep("Starting add products to cart and view cart test");

        // Create page objects
        LoginPage loginPage = new LoginPage();
        ProductsPage productsPage = new ProductsPage();
        CartPage cartPage = new CartPage();

        // Navigate and login
        logStep("Navigating to SauceDemo website");
        loginPage.navigateToLoginPage();

        logStep("Logging in with standard user");
        loginPage.login("standard_user", "secret_sauce");

        // Add multiple products to cart
        logStep("Adding backpack to cart");
        productsPage.addBackpackToCart();

        logStep("Adding bike light to cart");
        productsPage.addBikeLightToCart();

        logStep("Adding t-shirt to cart");
        productsPage.addTshirtToCart();

        // Navigate to cart
        logStep("Clicking on shopping cart link");
        productsPage.clickShoppingCart();

        // Verify cart page is displayed
        logStep("Verifying cart page is displayed");
        Assert.assertTrue(cartPage.isCartPageDisplayed(),
                "Cart page should be displayed");

        logStep("Verifying cart page title");
        Assert.assertEquals(cartPage.getPageTitle(), "Your Cart",
                "Page title should be 'Your Cart'");

        // Verify items in cart
        logStep("Verifying cart has 3 items");
        Assert.assertEquals(cartPage.getCartItemCount(), 3,
                "Cart should contain 3 items");

        logStep("Verifying first item name is in cart");
        String firstItemName = cartPage.getFirstItemName();
        Assert.assertFalse(firstItemName.isEmpty(),
                "First item name should not be empty");

        logStep("Verifying first item price is in cart");
        String firstItemPrice = cartPage.getFirstItemPrice();
        Assert.assertFalse(firstItemPrice.isEmpty(),
                "First item price should not be empty");

        logStep("Add products to cart and view cart test completed successfully");
    }

    @Test(priority = 2, description = "Verify user can remove items from cart")
    public void testRemoveItemsFromCart() {
        logStep("Starting remove items from cart test");

        // Create page objects
        LoginPage loginPage = new LoginPage();
        ProductsPage productsPage = new ProductsPage();
        CartPage cartPage = new CartPage();

        // Navigate and login
        logStep("Navigating to SauceDemo website");
        loginPage.navigateToLoginPage();

        logStep("Logging in with standard user");
        loginPage.login("standard_user", "secret_sauce");

        // Add multiple products to cart
        logStep("Adding backpack to cart");
        productsPage.addBackpackToCart();

        logStep("Adding bike light to cart");
        productsPage.addBikeLightToCart();

        logStep("Adding t-shirt to cart");
        productsPage.addTshirtToCart();

        // Navigate to cart
        logStep("Clicking on shopping cart link");
        productsPage.clickShoppingCart();

        // Verify items are added
        logStep("Verifying cart has 3 items");
        Assert.assertEquals(cartPage.getCartItemCount(), 3,
                "Cart should contain 3 items before removal");

        // Remove first item
        logStep("Removing first item from cart");
        cartPage.removeFirstItem();

        // Verify item is removed
        logStep("Verifying first item is removed");
        Assert.assertEquals(cartPage.getCartItemCount(), 2,
                "Cart should contain 2 items after removing one");

        // Remove remaining items
        logStep("Removing all remaining items from cart");
        cartPage.removeAllItems();

        // Verify cart is empty
        logStep("Verifying cart is empty");
        Assert.assertTrue(cartPage.isCartEmpty(),
                "Cart should be empty after removing all items");

        logStep("Remove items from cart test completed successfully");
    }

    @Test(priority = 3, description = "Verify user can continue shopping and proceed to checkout from cart")
    public void testNavigationFromCartPage() {
        logStep("Starting navigation from cart page test");

        // Create page objects
        LoginPage loginPage = new LoginPage();
        ProductsPage productsPage = new ProductsPage();
        CartPage cartPage = new CartPage();

        // Navigate and login
        logStep("Navigating to SauceDemo website");
        loginPage.navigateToLoginPage();

        logStep("Logging in with standard user");
        loginPage.login("standard_user", "secret_sauce");

        // Add product to cart
        logStep("Adding backpack to cart");
        productsPage.addBackpackToCart();

        // Navigate to cart
        logStep("Clicking on shopping cart link");
        productsPage.clickShoppingCart();

        // Verify cart page
        logStep("Verifying cart page is displayed");
        Assert.assertTrue(cartPage.isCartPageDisplayed(),
                "Cart page should be displayed");

        // Verify item is in cart
        logStep("Verifying item count is 1");
        Assert.assertEquals(cartPage.getCartItemCount(), 1,
                "Cart should contain 1 item");

        // Verify item details
        logStep("Verifying item name in cart");
        String itemName = cartPage.getFirstItemName();
        Assert.assertFalse(itemName.isEmpty(),
                "Item name should not be empty");

        logStep("Verifying item is found in cart");
        Assert.assertTrue(cartPage.isItemInCart(itemName),
                "Item should be found in cart using item name");

        // Proceed to checkout
        logStep("Clicking proceed to checkout button");
        cartPage.proceedToCheckout();

        logStep("Navigation from cart page test completed successfully");
    }
}

