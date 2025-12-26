package org.epam.utils;

import io.qameta.allure.Step;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class AllureUtils {

    private static WebDriver driver;

    public AllureUtils(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Capture screenshot for Allure reporting
     * @return byte array of screenshot
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] captureScreenshot() {
        if (driver != null) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    /**
     * Attach text information to Allure report
     * @param message text message to attach
     */
    @Attachment(value = "{0}", type = "text/plain")
    public static String attachText(String message) {
        return message;
    }

    /**
     * Log step information
     * @param stepDescription description of the step
     */
    @Step("Step: {stepDescription}")
    public static void logStep(String stepDescription) {
        System.out.println(">>> " + stepDescription);
    }

    /**
     * Get current page URL
     * @return current URL
     */
    @Step("Getting current URL")
    public static String getCurrentURL() {
        if (driver != null) {
            return driver.getCurrentUrl();
        }
        return "";
    }

}

