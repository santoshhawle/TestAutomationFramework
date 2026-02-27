package com.bddframework.driver;

import org.openqa.selenium.WebDriver;

public class DriverManager {

    public DriverManager() {
        String browser=System.getProperty("browser","chrome");
        //DriverFactory.getInstance().initDriver(browser);
    }

    public WebDriver getDriver(){
        return DriverFactory.getInstance().getDriver();
    }
}
