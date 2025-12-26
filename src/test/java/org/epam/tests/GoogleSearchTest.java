package org.epam.tests;

import io.qameta.allure.*;
import org.epam.utils.TestAllure;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

@Epic("Google Search")
@Feature("Search Functionality")
public class GoogleSearchTest {

    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        TestAllure.setDriver(driver);

        try {
            // Navigate to Google
            driver.get("https://www.google.com");
            TestAllure.captureScreenshot();

            // Find and capture search box
            WebElement searchBox = driver.findElement(By.name("q"));
            TestAllure.captureElementScreenshot(searchBox, "Search Box");

            // Enter search term
            searchBox.sendKeys("Selenium WebDriver");
            TestAllure.captureElementScreenshot(searchBox, "Search Box with Text");

            // Press Enter
            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(2000);

            // Capture search results
            TestAllure.captureSectionScreenshot(By.id("search"), "Search Results");

            System.out.println("Test completed successfully!");

        } catch (Exception e) {
            TestAllure.captureScreenshot();
            System.out.println("Test failed: " + e.getMessage());
        } finally {
            driver.quit();
            TestAllure.removeDriver();
        }
    }
}
