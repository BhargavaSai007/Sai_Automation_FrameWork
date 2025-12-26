# 05 – Logging and Reporting

## Logging
- API: SLF4J
- Backend: Log4j2

### Outputs
- Console
- Rolling file: `target/logs/test-automation.log`

### Why this helps in reviews
- Every failure has a timeline in logs.
- Retry attempts are visible (attempt number + exception type).

## Reporting
- Allure TestNG listener is configured in `pom.xml`.
- Cucumber uses Allure plugin + HTML/JSON outputs.

Artifacts:
- Allure results: `target/allure-results`
- Cucumber report: `target/cucumber-report/index.html`

