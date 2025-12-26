package org.epam.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Cucumber Runner - Executes BDD feature files
 *
 * Configuration:
 * - Features: src/test/resources/features/SauceDemo.feature
 * - Step Definitions: org.epam.stepdefinitions.*
 * - Format: pretty, html, json
 * - Reporting: Allure integration enabled
 *
 * Usage:
 * mvn test -Dtest=CucumberRunner
 * or
 * mvn verify -Dtest=CucumberRunner
 *
 * Features:
 * - Executes all scenarios in SauceDemo.feature
 * - Generates Allure reports
 * - Integrates with TestNG framework
 * - Parallel execution compatible
 */
@CucumberOptions(
        features = "src/test/resources/features/SauceDemo.feature",
        glue = "org.epam.stepdefinitions",
        plugin = {
                "pretty",
                "html:target/cucumber-report/index.html",
                "json:target/cucumber-report/cucumber.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true,
        publish = false,
        dryRun = false
)
public class CucumberRunner extends AbstractTestNGCucumberTests {

    /**
     * Data provider for parallel execution
     * Allows TestNG to run scenarios in parallel
     *
     * @return scenarios for parallel execution
     */
    @DataProvider(parallel = true)
    @Override
    public Object[][] scenarios() {
        return super.scenarios();
    }
}

