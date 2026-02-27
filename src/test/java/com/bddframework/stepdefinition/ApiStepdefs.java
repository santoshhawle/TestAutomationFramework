package com.bddframework.stepdefinition;

import com.bddframework.api.auth.AuthUtil;
import com.bddframework.api.client.GetBookingClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

public class ApiStepdefs {

    TestContext context;

    public ApiStepdefs(TestContext context) {
        this.context = context;
    }

    @Given("user get access token")
    public void userGetAccessToken() throws JsonProcessingException {
        String token = AuthUtil.getToken();
        System.out.println(token);
        context.setTestData("token",token);
    }

    @When("user run the get bookingid get api")
    public void userRunTheGetBookingidGetApi() {
        context.getTestData("token");
        GetBookingClient getBookingClient=new GetBookingClient();
        Response res = getBookingClient.getBookingIds();
        Assert.assertEquals(res.getStatusCode(),200);

    }

    @Then("verify booking ids are not null")
    public void verifyBookingIdsAreNotNull() {

    }
}
