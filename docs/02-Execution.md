# 02 – Execution (How to run)

## Normal full run
```cmd
mvn clean test
```

## Browser selection
Browser is read from:
- `-Dbrowser=chrome|firefox|edge` (highest priority)
- else `config.properties` key: `browser=`

Examples:
```cmd
mvn clean test -Dbrowser=chrome
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
```

## Parallel
Surefire is configured for parallel **methods** with thread count `parallel.count`.
Example:
```cmd
mvn test -Dparallel.count=3
```

## Run only TestNG suite XML
```cmd
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

## Run only Cucumber
```cmd
mvn test -Dtest=org.epam.runners.CucumberRunner
```

## Artifacts
- Logs: `target/logs/test-automation.log`
- Allure results: `target/allure-results`
- Cucumber report: `target/cucumber-report/index.html`

