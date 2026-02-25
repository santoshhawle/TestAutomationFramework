package com.bddframework.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.xml.XmlSuite;

@CucumberOptions(
        features = "classpath:features",
        glue = {"com.bddframework.stepdefinition","com.bddframework.hooks"},
        plugin = {"pretty", "html:target/cucumber.html"},
        tags = "@login"
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
}
