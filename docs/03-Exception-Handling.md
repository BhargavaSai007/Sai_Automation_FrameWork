# 03 – Exception Handling (How to explain to your lead)

## The approach (one sentence)
We **never let raw Selenium exceptions leak out of page actions**; we **translate** them into **framework custom exceptions** with meaningful messages, after applying **retry**.

## Why this matters
- Raw Selenium exceptions are noisy and inconsistent.
- Custom exceptions make failures:
  - easier to debug
  - easier to report
  - easier to maintain

## Exception taxonomy (your current code)
All custom exceptions extend `FrameworkException`.

| Custom exception | Purpose | Typical origin |
|---|---|---|
| `ConfigurationException` | config is missing/invalid | parsing timeout, invalid browser |
| `NavigationException` | navigation failures | bad URL, blocked navigation |
| `TimeoutException` | explicit waits exceeded | element not visible/clickable, page load |
| `ElementNotFoundException` | element not found / stale / not interactable | bad locator, dynamic DOM |
| `FrameworkException` | generic wrapper | final retry failure, unexpected errors |

## Where exceptions are thrown (mapping to code)

### A) Driver lifecycle
- `DriverManager.getDriver()`
  - throws `FrameworkException` when driver is not initialized.
- `DriverManager.createDriver()`
  - throws `ConfigurationException` when browser name is empty/invalid.
  - wraps unexpected driver creation errors into `FrameworkException`.

### B) Page actions (BasePage)
`BasePage` catches Selenium exceptions and throws your custom ones.
Examples:
- `org.openqa.selenium.TimeoutException` → `org.epam.exceptions.TimeoutException`
- `org.openqa.selenium.NoSuchElementException` → `ElementNotFoundException`
- `org.openqa.selenium.StaleElementReferenceException` → `ElementNotFoundException` (treat as DOM instability)

### C) Retry final failure
`RetryUtils.retryOperation(...)`:
- logs every attempt
- on final failure throws `FrameworkException` with:
  - operation name
  - attempts count
  - total duration
  - last exception message

## Where exceptions are handled (catch points)

### BaseTest
- In `setUp()`:
  - catches `FrameworkException` → logs + rethrows (fail fast)
  - catches other `Exception` → wraps into `FrameworkException`

- In `tearDown()`:
  - logs cleanup failure but does not crash remaining tests

## How to handle exceptions using this approach (practical rules)

### Rule 1: Page objects should throw framework exceptions, not Selenium exceptions
If a page method fails, it should throw one of:
- `ElementNotFoundException`
- `TimeoutException`
- `NavigationException`

### Rule 2: Tests should not do huge try/catch blocks
Tests should read cleanly:
- call page methods
- assert outcome

Only catch exceptions in tests if you are validating a negative scenario (expected failure).

### Rule 3: Always include context in the exception message
Good message contains:
- the action ("Click login button")
- the locator or data
- the expectation ("should be visible")

## Known gap (talk-track)
Your `BrowserFactory` is inconsistent:
- Chrome uses WebDriverManager
- Firefox uses a hardcoded `geckodriver.exe` path and a hardcoded Firefox binary path

This can cause environment-specific failures. The recommended improvement is to use WebDriverManager for Firefox/Edge too.

