package com.bddframework.stepdefinition;

import com.bddframework.pageObject.HomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.testng.Assert;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

public class LoginStepdefs {

    private HomePage homePage;
    private TestContext context;

    public LoginStepdefs(TestContext context, HomePage homePage) {
        this.context = context;
        this.homePage=homePage;
    }

    @Given("user launch the application")
    public void userLaunchTheApplication() {
        context.getDriver().get("https://sauce-demo.myshopify.com/");
        context.getDriver().quit();
    }

    @When("user login to application with valid credentials")
    public void userLoginToApplicationWithValidCredentials() {
       homePage.clickOnLogin()
               .serUsernameFiled("username")
               .getPasswordField("password")
               .getSignInBtn();
    }

    @Then("verify user is successfully logged in")
    public void verifyUserIsSuccessfullyLoggedIn() {
    }
}
