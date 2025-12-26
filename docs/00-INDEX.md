# Documentation Index (Lead Review Pack)

This folder is the single source of truth for how the framework works.

## Must-read (for lead review)
1. `01-Architecture.md` – end-to-end framework flow
2. `02-Execution.md` – how to run tests (TestNG + Cucumber), parallel, browsers
3. `03-Exception-Handling.md` – **how exceptions are handled in this framework**
4. `04-Retry-And-Waits.md` – retry policy (MAX=3) and wait strategy
5. `05-Logging-Reporting.md` – Log4j2 + Allure + artifacts
6. `06-Design-Patterns.md` – Singleton/Factory/POM and where exactly used
7. `07-FAQ.md` – common lead questions + ready answers
8. `99-REVIEW-SUMMARY.md` – **🎯 FINAL REVIEW PREP (start here for lead review)**

## Notes about current state
- Your framework already contains custom exceptions and a global retry wrapper used by most page actions.
- This doc set is written to match the **actual code** (`BasePage`, `RetryUtils`, `DriverManager`, `BrowserFactory`, `ConfigManager`).
- All documentation is aligned with your actual implementation - no contradictions or placeholder content.
