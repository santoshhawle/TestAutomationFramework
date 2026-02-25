package com.bddframework.driver;

import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {

    private DriverFactory(){

    }

    // Singleton (thread-safe via class loader)
    private static class Holder {
        private static final DriverFactory INSTANCE = new DriverFactory();
    }

    public static DriverFactory getInstance() {
        return Holder.INSTANCE;
    }

    //Thread local for parallel execution
    private static ThreadLocal<WebDriver> driver=new ThreadLocal<>();

    public void initDriver(String browserName){
        WebDriver webDriver=switch (browserName.toLowerCase()){
            case "chrome" -> new ChromeDriver();
            case "firefox" -> new FirefoxDriver();
            default -> throw new InvalidArgumentException("Invalid browser name provided");
        };
        driver.set(webDriver);
    }

    public WebDriver getDriver(){
        return driver.get();
    }

    public void quitDriver(){
        if(driver.get()!=null){
            driver.get().quit();
            driver.remove();
        }
    }

}
