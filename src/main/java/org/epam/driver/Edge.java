package org.epam.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class Edge {

    public static void main(String[] args) {
        System.setProperty("SE_MSEDGEDRIVER_MIRROR_URL", "https://msedgedriver.microsoft.com");
        EdgeOptions options = new EdgeOptions();

        options.addArguments("--start-maximized");
        WebDriver driver =new EdgeDriver(options);
        driver.get("http://google.com");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
