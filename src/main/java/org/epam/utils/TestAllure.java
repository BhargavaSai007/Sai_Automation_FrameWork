package org.epam.utils;

import io.qameta.allure.Step;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;

public class TestAllure {

    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public TestAllure(WebDriver driver) {
        setDriver(driver);
    }

    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void removeDriver() {
        driverThreadLocal.remove();
    }

    @Attachment(value = "Full Page Screenshot", type = "image/png")
    public static byte[] captureScreenshot() {
        WebDriver driver = getDriver();
        if (driver != null) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    @Attachment(value = "Element Screenshot: {elementName}", type = "image/png")
    public static byte[] captureElementScreenshot(WebElement element, String elementName) {
        if (element != null) {
            return element.getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    @Attachment(value = "Section Screenshot: {sectionName}", type = "image/png")
    public static byte[] captureSectionScreenshot(By locator, String sectionName) {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                WebElement element = driver.findElement(locator);
                return element.getScreenshotAs(OutputType.BYTES);
            } catch (NoSuchElementException e) {
                attachText("Element not found: " + locator.toString());
                return captureScreenshot();
            }
        }
        return new byte[0];
    }

    @Attachment(value = "{0}", type = "text/plain")
    public static String attachText(String message) {
        return message;
    }

    @Step("Step: {stepDescription}")
    public static void logStep(String stepDescription) {
        System.out.println(">>> " + stepDescription);
    }

    @Step("Getting current URL")
    public static String getCurrentURL() {
        WebDriver driver = getDriver();
        if (driver != null) {
            return driver.getCurrentUrl();
        }
        return "";
    }
}
