package org.epam.pages;

import org.epam.exceptions.ElementNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Products Page Object
 *
 * Encapsulates all products page elements and actions
 * Extends BasePage for retry mechanism and common operations
 */
public class ProductsPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(ProductsPage.class);

//    @FindBy(className = "inventory_container")  // More specific
//    private WebElement inventoryContainer;

    // Page elements (locators)
    private final By productsContainer = By.className("inventory");
    private final By inventoryContainer = By.className("inventory_container");
    private final By productItem = By.className("inventory_item");
    private final By backpackAddButton = By.id("add-to-cart-sauce-labs-backpack");
    private final By bikeLightAddButton = By.id("add-to-cart-sauce-labs-bike-light");
    private final By tshirtAddButton = By.id("add-to-cart-sauce-labs-bolt-t-shirt");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By shoppingCartLink = By.className("shopping_cart_link");

    /**
     * Check if products page is displayed
     * @return true if products container is visible
     */
    public boolean isProductsPageDisplayed() {
        try {
            // Wait for URL to contain 'inventory.html' first
            waitForUrlContains("inventory.html");

            // Then check for inventory container
            return isElementDisplayed(inventoryContainer);
        } catch (Exception e) {
            logger.error("Products page not displayed", e);
            return false;
        }
    }


    /**
     * Add backpack product to cart
     * @throws ElementNotFoundException if button not found
     */
    public void addBackpackToCart() {
        logger.info("Adding backpack to cart");
        try {
            clickElement(backpackAddButton);
            logger.info("Backpack added to cart successfully");
        } catch (ElementNotFoundException e) {
            logger.error("Failed to add backpack to cart: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Add bike light product to cart
     * @throws ElementNotFoundException if button not found
     */
    public void addBikeLightToCart() {
        logger.info("Adding bike light to cart");
        try {
            clickElement(bikeLightAddButton);
            logger.info("Bike light added to cart successfully");
        } catch (ElementNotFoundException e) {
            logger.error("Failed to add bike light to cart: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Add t-shirt product to cart
     * @throws ElementNotFoundException if button not found
     */
    public void addTshirtToCart() {
        logger.info("Adding t-shirt to cart");
        try {
            clickElement(tshirtAddButton);
            logger.info("T-shirt added to cart successfully");
        } catch (ElementNotFoundException e) {
            logger.error("Failed to add t-shirt to cart: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Get cart item count from badge
     * @return number of items in cart as string
     */
    public String getCartItemCount() {
        logger.debug("Getting cart item count");
        try {
            String count = getElementText(cartBadge);
            logger.info("Cart item count: {}", count);
            return count;
        } catch (ElementNotFoundException e) {
            logger.debug("Cart badge not found - cart is empty: {}", e.getMessage());
            return "0";
        }
    }

    /**
     * Click on shopping cart link to go to cart page
     * @throws ElementNotFoundException if cart link not found
     */
    public void clickShoppingCart() {
        logger.info("Clicking shopping cart link");
        try {
            clickElement(shoppingCartLink);
            logger.info("Navigated to shopping cart");
        } catch (ElementNotFoundException e) {
            logger.error("Failed to click shopping cart: {}", e.getMessage());
            throw e;
        }
    }
}

