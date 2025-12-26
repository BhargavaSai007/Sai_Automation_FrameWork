package org.epam.driver;

/**
 * Enum for supported browser types
 *
 * Usage:
 * BrowserType.CHROME
 * BrowserType.FIREFOX
 * BrowserType.EDGE
 *
 * Benefits:
 * - Type-safe: Can't make typos like "chrom" instead of "chrome"
 * - Auto-complete in IDE: Just type BrowserType. and see all options
 * - Compile-time checking: Wrong browser types won't compile
 */
public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge");

    private final String browserName;

    /**
     * Constructor
     * @param browserName the command-line parameter value (e.g., "chrome")
     */
    BrowserType(String browserName) {
        this.browserName = browserName;
    }

    /**
     * Get the string value of browser
     * @return browser name as string
     */
    public String getBrowserName() {
        return browserName;
    }

    /**
     * Convert string to BrowserType enum
     *
     * Example:
     * BrowserType type = BrowserType.fromString("firefox");
     *
     * @param browserName string value (e.g., "firefox")
     * @return matching BrowserType enum, or CHROME as default
     */
    public static BrowserType fromString(String browserName) {
        if (browserName == null || browserName.isEmpty()) {
            return CHROME; // default
        }

        for (BrowserType type : BrowserType.values()) {
            if (type.browserName.equalsIgnoreCase(browserName)) {
                return type;
            }
        }

        // If no match found, return CHROME as default
        return CHROME;
    }
}

