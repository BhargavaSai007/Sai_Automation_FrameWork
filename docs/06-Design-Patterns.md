# 06 – Design Patterns (mapped to your code)

## Singleton (ThreadLocal)
- Implemented by `DriverManager` using `ThreadLocal<WebDriver>`.
- Effect: one driver per thread for parallel execution.

## Factory
- Implemented by `BrowserFactory.createDriver(BrowserType)`.
- Central place for browser options.

## Page Object Model
- Implemented by classes in `org.epam.pages`.
- `BasePage` provides reusable actions.

## Retry pattern
- Implemented by `RetryUtils.retryOperation(...)`.
- Used by `BasePage` methods.

