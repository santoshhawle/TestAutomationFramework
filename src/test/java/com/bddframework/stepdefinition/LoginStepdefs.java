package com.bddframework.stepdefinition;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginStepdefs {

    private TestContext context;

    public LoginStepdefs(TestContext context) {
        this.context = context;
    }

    @Given("user launch the application")
    public void userLaunchTheApplication() {
        context.getDriver().get("https://google.com");
    }

    @When("user login to application with valid credentials")
    public void userLoginToApplicationWithValidCredentials() {

    }

    @Then("verify user is successfully logged in")
    public void verifyUserIsSuccessfullyLoggedIn() {
    }
}
