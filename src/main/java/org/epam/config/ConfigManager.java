package org.epam.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.epam.exceptions.ConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration Manager
 * Loads and manages all configuration properties from config.properties
 * Uses SLF4J for logging all configuration activities
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    // Load properties when class is first used
    static {
        logger.info("Initializing ConfigManager");
        loadProperties();
        logger.info("ConfigManager initialized successfully");
    }

    private static void loadProperties() {
        properties = new Properties();
        try {
            logger.debug("Loading configuration from: {}", CONFIG_FILE_PATH);
            FileInputStream file = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(file);
            file.close();
            logger.info("Configuration loaded successfully. Properties count: {}", properties.size());
        } catch (IOException e) {
            logger.warn("Config file not found at: {}. Using default values", CONFIG_FILE_PATH);
            setDefaultProperties();
        }
    }

    private static void setDefaultProperties() {
        logger.debug("Setting default configuration properties");
        properties.setProperty("base.url", "https://www.saucedemo.com");
        properties.setProperty("browser", "chrome");
        properties.setProperty("timeout", "10");
        properties.setProperty("headless", "false");
        logger.info("Default properties set");
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property '{}' not found in configuration", key);
            return "";
        }
        logger.debug("Retrieved property: {} = {}", key, value);
        return value;
    }

    public static String getBaseUrl() {
        String url = getProperty("base.url");
        logger.info("Base URL: {}", url);
        return url;
    }

    public static String getBrowser() {
        String browser = System.getProperty("browser", getProperty("browser"));
        logger.info("Browser set to: {}", browser);
        return browser;
    }

    public static int getTimeout() {
        try {
            int timeout = Integer.parseInt(getProperty("timeout"));
            logger.debug("Timeout value: {} seconds", timeout);
            return timeout;
        } catch (NumberFormatException e) {
            logger.error("Invalid timeout value in configuration", e);
            throw new ConfigurationException("Invalid timeout value. Must be a number", e);
        }
    }

    public static boolean isHeadless() {
        String headless = getProperty("headless");
        boolean isHeadless = Boolean.parseBoolean(headless);
        logger.debug("Headless mode: {}", isHeadless);
        return isHeadless;
    }
}