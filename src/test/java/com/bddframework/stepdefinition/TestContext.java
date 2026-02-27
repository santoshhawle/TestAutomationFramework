package com.bddframework.stepdefinition;

import com.bddframework.driver.DriverManager;
import io.restassured.response.Response;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class TestContext {

    private WebDriver driver;
    public Response response;
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

    @SuppressWarnings("unchecked")
    public <T> T getTestData(String key) {
        return (T) testData.get(key);
    }

    public void setTestData(String key, Object value) {
         testData.put(key, value);
    }
}
