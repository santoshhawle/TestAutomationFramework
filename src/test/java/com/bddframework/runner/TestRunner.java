package com.bddframework.runner;

import io.cucumber.java.Before;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.flywaydb.core.Flyway;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.xml.XmlSuite;

@CucumberOptions(
        features = "classpath:features",
        glue = {"com.bddframework.stepdefinition","com.bddframework.hooks"},
        plugin = {
                "pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "@apir"
        )
public class TestRunner extends AbstractTestNGCucumberTests {

    @DataProvider(parallel = true)
    @Override
    public Object[][] scenarios() {
        return super.scenarios();
    }

    public void setUp(ITestContext context){
        XmlSuite xmlSuite = context.getSuite().getXmlSuite();
        xmlSuite.setParallel(XmlSuite.ParallelMode.TESTS);
        xmlSuite.setDataProviderThreadCount(2);
        xmlSuite.setThreadCount(2);
    }

    @Before(order = 1)
    public void migrateDB(){
        Flyway flyway = Flyway.configure()
                .dataSource(
                        "jdbc:mysql://localhost:3306/company_big",
                        "root",
                        "root")
                .load();

        flyway.clean();      // optional for test environments
        flyway.migrate();    // run migrations
    }
    }
}
