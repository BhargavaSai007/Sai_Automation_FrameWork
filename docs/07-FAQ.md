# 07 – Lead Review FAQ (answers you can use)

## Q: How do you support parallel execution?
`DriverManager` uses `ThreadLocal<WebDriver>`, so every parallel test thread has its own driver.

## Q: Where is retry implemented?
In `RetryUtils`, used by `BasePage` for waits/click/type/read.

## Q: Why custom exceptions?
To convert noisy Selenium errors into a small set of meaningful failure types (`TimeoutException`, `ElementNotFoundException`, etc.) with context.

## Q: How do you debug a failure quickly?
1) Check `target/logs/test-automation.log` for the failed step
2) Find retry attempts and the last thrown exception
3) If configured, check screenshot path taken by `TestUtils`
4) Check Allure report timeline

## Q: What would you improve next?
Make Firefox/Edge driver setup consistent using WebDriverManager (remove hardcoded paths).

