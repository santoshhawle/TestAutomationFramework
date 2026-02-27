package com.bddframework.hooks;

import com.bddframework.stepdefinition.TestContext;
import io.cucumber.java.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    private TestContext context;

    public Hooks( TestContext context) {
        this.context = context;
    }

    @Before
    public void setUp(Scenario scenario){
        System.out.println("Before");
    }

    @AfterStep
    public void afterStep(Scenario scenario){
        if(scenario.isFailed()){
            byte[] screenshot = ((TakesScreenshot) context.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot,"image/png",scenario.getName());
        }
    }

    @After
    public void tearDown(Scenario scenario){

    }

}
