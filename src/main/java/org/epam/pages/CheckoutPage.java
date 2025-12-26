package org.epam.pages;

import org.epam.exceptions.ElementNotFoundException;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checkout Page Object
 *
 * Encapsulates checkout process elements and actions
 * Two main checkout steps:
 * 1. Checkout Information (personal details)
 * 2. Checkout Overview (review order)
 *
 * Features:
 * - Enter shipping information
 * - Validate form errors
 * - Review order summary
 * - Complete purchase
 * - Cancel order
 */
public class CheckoutPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CheckoutPage.class);

    // Step 1: Checkout Information Page
    private final By pageTitle = By.xpath("//span[@class='title']");
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    // Step 2: Checkout Overview Page
    private final By finishButton = By.id("finish");
    private final By orderSummary = By.className("summary_info");
    private final By summaryItemTotal = By.className("summary_subtotal_label");
    private final By summaryTax = By.className("summary_tax_label");
    private final By summaryTotal = By.className("summary_total_label");
    private final By backButton = By.id("back");

    // Success Page
    private final By orderCompleteHeader = By.className("complete-header");
    private final By orderCompleteText = By.className("complete-text");

    /**
     * Verify if checkout information page is displayed
     *
     * @return true if on checkout information step
     */
    public boolean isCheckoutInformationPageDisplayed() {
        logger.debug("Checking if checkout information page is displayed");
        boolean isDisplayed = isElementDisplayed(pageTitle) &&
                            getElementText(pageTitle).contains("Checkout");
        logger.info("Checkout information page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Verify if checkout overview page is displayed
     *
     * @return true if on checkout overview step
     */
    public boolean isCheckoutOverviewPageDisplayed() {
        logger.debug("Checking if checkout overview page is displayed");
        boolean isDisplayed = getCurrentUrl().contains("checkout-step-two");
        logger.info("Checkout overview page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Get page title
     *
     * @return page title text
     */
    public String getPageTitle() {
        logger.debug("Retrieving checkout page title");
        try {
            String title = getElementText(pageTitle);
            logger.info("Checkout page title: {}", title);
            return title;
        } catch (Exception e) {
            logger.warn("Could not get page title");
            return "";
        }
    }

    /**
     * Enter first name
     *
     * @param firstName customer first name
     */
    public void enterFirstName(String firstName) {
        logger.info("Entering first name: {}", firstName);
        typeText(firstNameField, firstName);
        logger.debug("First name entered");
    }

    /**
     * Enter last name
     *
     * @param lastName customer last name
     */
    public void enterLastName(String lastName) {
        logger.info("Entering last name: {}", lastName);
        typeText(lastNameField, lastName);
        logger.debug("Last name entered");
    }

    /**
     * Enter postal code
     *
     * @param postalCode customer postal code
     */
    public void enterPostalCode(String postalCode) {
        logger.info("Entering postal code: {}", postalCode);
        typeText(postalCodeField, postalCode);
        logger.debug("Postal code entered");
    }

    /**
     * Complete checkout information step
     * Fills in all required fields and clicks continue
     *
     * @param firstName customer first name
     * @param lastName customer last name
     * @param postalCode customer postal code
     */
    public void fillCheckoutInformation(String firstName, String lastName, String postalCode) {
        logger.info("Filling checkout information - First: {}, Last: {}, Postal: {}",
                   firstName, lastName, postalCode);

        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);

        logger.debug("All checkout information filled");
    }

    /**
     * Click continue button to proceed to overview
     *
     * @throws ElementNotFoundException if button not found
     */
    public void clickContinue() {
        logger.info("Clicking continue button");
        clickElement(continueButton);
        logger.info("Proceeding to checkout overview");
    }

    /**
     * Click cancel button to go back
     *
     * @throws ElementNotFoundException if button not found
     */
    public void clickCancel() {
        logger.info("Clicking cancel button");
        clickElement(cancelButton);
        logger.info("Checkout cancelled");
    }

    /**
     * Get error message text
     *
     * @return error message (empty string if no error)
     */
    public String getErrorMessage() {
        logger.debug("Retrieving error message");
        try {
            if (isElementDisplayed(errorMessage)) {
                String message = getElementText(errorMessage);
                logger.info("Error message: {}", message);
                return message;
            }
        } catch (Exception e) {
            logger.debug("No error message displayed");
        }
        return "";
    }

    /**
     * Check if error message is displayed
     *
     * @return true if error message visible
     */
    public boolean isErrorMessageDisplayed() {
        logger.debug("Checking if error message is displayed");
        boolean isDisplayed = isElementDisplayed(errorMessage);
        logger.info("Error message displayed: {}", isDisplayed);
        return isDisplayed;
    }

    // ============ CHECKOUT OVERVIEW METHODS ============

    /**
     * Get order subtotal
     *
     * @return subtotal text (e.g., "$29.99")
     */
    public String getOrderSubtotal() {
        logger.debug("Retrieving order subtotal");
        try {
            String subtotal = getElementText(summaryItemTotal);
            logger.info("Order subtotal: {}", subtotal);
            return subtotal;
        } catch (Exception e) {
            logger.warn("Could not retrieve subtotal");
            return "";
        }
    }

    /**
     * Get order tax amount
     *
     * @return tax text (e.g., "$2.40")
     */
    public String getOrderTax() {
        logger.debug("Retrieving order tax");
        try {
            String tax = getElementText(summaryTax);
            logger.info("Order tax: {}", tax);
            return tax;
        } catch (Exception e) {
            logger.warn("Could not retrieve tax");
            return "";
        }
    }

    /**
     * Get order total (subtotal + tax)
     *
     * @return total text (e.g., "$32.39")
     */
    public String getOrderTotal() {
        logger.debug("Retrieving order total");
        try {
            String total = getElementText(summaryTotal);
            logger.info("Order total: {}", total);
            return total;
        } catch (Exception e) {
            logger.warn("Could not retrieve total");
            return "";
        }
    }

    /**
     * Click finish button to complete purchase
     *
     * @throws ElementNotFoundException if button not found
     */
    public void clickFinish() {
        logger.info("Clicking finish button to complete purchase");
        clickElement(finishButton);
        logger.info("Purchase completed");
    }

    /**
     * Click back button to return to cart
     *
     * @throws ElementNotFoundException if button not found
     */
    public void clickBack() {
        logger.info("Clicking back button");
        clickElement(backButton);
        logger.info("Returning to cart");
    }

    // ============ ORDER COMPLETE PAGE METHODS ============

    /**
     * Verify if order was completed successfully
     *
     * @return true if order complete page displayed
     */
    public boolean isOrderCompletePageDisplayed() {
        logger.debug("Checking if order complete page is displayed");
        boolean isDisplayed = getCurrentUrl().contains("checkout-complete");
        logger.info("Order complete page displayed: {}", isDisplayed);
        return isDisplayed;
    }

    /**
     * Get order complete message
     * Usually "Thank you for your order!"
     *
     * @return order complete message
     */
    public String getOrderCompleteMessage() {
        logger.debug("Retrieving order complete message");
        try {
            String message = getElementText(orderCompleteHeader);
            logger.info("Order complete message: {}", message);
            return message;
        } catch (Exception e) {
            logger.warn("Could not retrieve order complete message");
            return "";
        }
    }

    /**
     * Get detailed order completion text
     * Usually contains order confirmation details
     *
     * @return completion details text
     */
    public String getOrderCompletionDetails() {
        logger.debug("Retrieving order completion details");
        try {
            String details = getElementText(orderCompleteText);
            logger.info("Order completion details: {}", details);
            return details;
        } catch (Exception e) {
            logger.warn("Could not retrieve order completion details");
            return "";
        }
    }
}

