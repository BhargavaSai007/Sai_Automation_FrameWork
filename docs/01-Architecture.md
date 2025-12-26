# 01 – Architecture
- Cleanup always calls `driver.remove()` in `DriverManager.quitDriver()`.
- No shared static driver in page objects.
- Each thread gets its own `WebDriver` via `ThreadLocal<WebDriver>`.
## Parallel execution (why it works)

5. `BaseTest.tearDown()` → `DriverManager.quitDriver()`
4. `BasePage` wraps Selenium calls with retry + custom exceptions
3. Page calls `BasePage` helper methods
2. Test calls Page Objects
1. `BaseTest.setUp()` → `DriverManager.createDriver()`
## Execution flow (what happens in one test)

- Custom exceptions: `FrameworkException` + specialized subtypes.
- `RetryUtils` wraps operations with max retries.
### 4) Utilities + Exception layer

- `BrowserFactory` = creates Chrome/Firefox/Edge drivers.
- `DriverManager` = ThreadLocal driver lifecycle (parallel-safe).
### 3) Driver layer (`src/main/java/org/epam/driver`)

- `BasePage` provides reusable operations (wait/click/type/read/navigate).
- Page Objects encapsulate locators + actions.
### 2) Page layer (`src/main/java/org/epam/pages`)

- BDD: `CucumberRunner` + step definitions
- TestNG tests: `LoginTest`, `CartTest`, `ShoppingFlowTest`
- `BaseTest` controls setup/teardown.
### 1) Test layer (`src/test/java`)

## Layered architecture

- Allure reporting
- Log4j2 (console + file)
- Cucumber (BDD)
- TestNG (parallel)
- Selenium WebDriver 4
- Java 17 + Maven
Automate SauceDemo UI flows using:
## Project goal


