package org.epam.utils;

import org.apache.commons.io.FileUtils;
import org.epam.driver.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestUtils {

    // Take screenshot and save to file
    public static String takeScreenshot(String testName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) DriverManager.getDriver();
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = "test-output/screenshots/" + fileName;

            File destFile = new File(filePath);
            destFile.getParentFile().mkdirs(); // Create directories if they don't exist

            FileUtils.copyFile(sourceFile, destFile);
            System.out.println("Screenshot saved: " + filePath);

            return filePath;
        } catch (IOException e) {
            System.out.println("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }

    // Wait for a specified number of seconds
    public static void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Wait interrupted: " + e.getMessage());
        }
    }

    // Generate random email
    public static String generateRandomEmail() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "test" + timestamp + "@example.com";
    }

    // Generate current timestamp
    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Print test step information
    public static void logTestStep(String stepDescription) {
        System.out.println("[" + getCurrentTimestamp() + "] " + stepDescription);
    }
}