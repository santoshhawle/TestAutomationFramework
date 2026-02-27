package com.bddframework.stepdefinition;

import com.bddframework.driver.DriverManager;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class TestContext {

    private WebDriver driver;

    private final Map<String, Object> testData=new HashMap<>();

    public TestContext(DriverManager driverManager) {
        this.driver = driverManager.getDriver();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public String getTestData(String key) {
        return testData.get(key).toString();
    }

    public Map<String, Object> setTestData(String key, Object value) {
        return (Map<String, Object>) testData.put(key, value);
    }
}
