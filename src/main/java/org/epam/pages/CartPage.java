package org.epam.pages;

import org.epam.exceptions.ElementNotFoundException;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shopping Cart Page Object
 *
 * Encapsulates all shopping cart page elements and actions
 * Extends BasePage for retry mechanism and common operations
 */
public class CartPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);

    // Page elements (locators)
    private final By cartContainer = By.className("cart_list");
    private final By cartItem = By.className("cart_item");
    private final By removeButton = By.xpath("//button[contains(@id, 'remove')]");
    private final By cartPageTitle = By.className("title");
    private final By cartEmptyMessage = By.className("complete-text");

    /**
     * Check if cart page is displayed
     * @return true if cart container is visible
     */
    public boolean isCartPageDisplayed() {
        logger.debug("Checking if cart page is displayed");
        boolean isDisplayed = isElementDisplayed(cartContainer);
        logger.info("Cart page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Get number of items in cart
     * @return count of cart items
     */
    public int getCartItemCount() {
        logger.debug("Getting cart item count");
        try {
            // Find all cart items
            int count = driver.findElements(cartItem).size();
            logger.info("Cart item count: {}", count);
            return count;
        } catch (Exception e) {
            logger.debug("Error getting cart item count: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Check if specific item is in cart
     * @param itemName name of item to check
     * @return true if item is in cart
     */
    public boolean isItemInCart(String itemName) {
        logger.debug("Checking if item is in cart: {}", itemName);
        try {
            By itemLocator = By.xpath("//div[contains(text(), '" + itemName + "')]");
            boolean isPresent = isElementDisplayed(itemLocator);
            logger.info("Item '{}' in cart: {}", itemName, isPresent);
            return isPresent;
        } catch (ElementNotFoundException e) {
            logger.debug("Item not found in cart: {}", itemName);
            return false;
        }
    }

    /**
     * Remove first item from cart
     * @throws ElementNotFoundException if remove button not found
     */
    public void removeFirstItem() {
        logger.info("Removing first item from cart");
        try {
            clickElement(removeButton);
            logger.info("Item removed from cart successfully");
        } catch (ElementNotFoundException e) {
            logger.error("Failed to remove item from cart: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Check if cart is empty
     * @return true if no items in cart
     */
    public boolean isCartEmpty() {
        logger.debug("Checking if cart is empty");
        boolean isEmpty = getCartItemCount() == 0;
        logger.info("Cart empty: {}", isEmpty);
        return isEmpty;
    }

    /**
     * Get the name of the first item in cart
     * @return item name text
     */
    public String getFirstItemName() {
        logger.debug("Getting first item name from cart");
        By itemName = By.className("inventory_item_name");
        return getElementText(itemName);
    }

    /**
     * Get the price of the first item in cart
     * @return item price text
     */
    public String getFirstItemPrice() {
        logger.debug("Getting first item price from cart");
        By itemPrice = By.className("inventory_item_price");
        return getElementText(itemPrice);
    }

    /**
     * Get the page title
     * @return page title text
     */
    public String getPageTitle() {
        logger.debug("Getting cart page title");
        return getElementText(cartPageTitle);
    }

    /**
     * Remove all items from cart
     */
    public void removeAllItems() {
        logger.info("Removing all items from cart");
        while (getCartItemCount() > 0) {
            removeFirstItem();
        }
        logger.info("All items removed from cart");
    }

    /**
     * Proceed to checkout
     * @throws ElementNotFoundException if checkout button not found
     */
    public void proceedToCheckout() {
        logger.info("Proceeding to checkout");
        try {
            By checkoutButton = By.id("checkout");
            clickElement(checkoutButton);
            logger.info("Navigated to checkout page");
        } catch (ElementNotFoundException e) {
            logger.error("Failed to proceed to checkout: {}", e.getMessage());
            throw e;
        }
    }
}

