package com.bddframework.stepdefinition;

import com.bddframework.api.auth.AuthUtil;
import com.bddframework.api.client.APIClient;
import com.bddframework.api.client.CreateBookingClient;
import com.bddframework.api.client.GetBookingClient;
import com.bddframework.api.payloads.Booking;
import com.bddframework.api.payloads.BookingDates;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.PendingException;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.Map;

public class ApiStepdefs {

    TestContext context;

    public ApiStepdefs(TestContext context) {
        this.context = context;
    }

    @Given("user get access token")
    public void userGetAccessToken() throws JsonProcessingException {
        String token = AuthUtil.getToken();
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
        System.out.println("");
    }

    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String endpoint) {
        APIClient apiClient=new APIClient();
        Response response = apiClient.get(endpoint);
        context.response=response;
        context.setTestData("statusCode",response.getStatusCode());
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        int actualStatusCode= context.getTestData("statusCode");
        Assert.assertEquals(actualStatusCode,statusCode);
    }

    @And("the response body should be empty")
    public void theResponseBodyShouldBeEmpty() {
        Assert.assertFalse(context.response.body().toString().isEmpty(),
                "Expected response body to NOT be empty");
    }

    @When("I send a POST request to {string} with body:")
    public void iSendAPOSTRequestToWithBody(String endpoint, String payload) {
        APIClient apiClient=new APIClient();
        Response post = apiClient.post(endpoint, payload);
    }

    @And("the response should contain field {string}")
    public void theResponseShouldContainField(String arg0) {
        int bookingId =context.response.then().extract().path("bookingid");
        Assert.assertTrue(bookingId>0);
    }

    @And("the response should contain object {string}")
    public void theResponseShouldContainObject(String arg0) {

    }

    @And("the response field {string} should equal {string}")
    public void theResponseFieldShouldEqual(String arg0, String arg1) {

    }

    @And("the response field {string} should equal {int}")
    public void theResponseFieldShouldEqual(String arg0, int arg1) {

    }

    @And("the response field {string} should equal true")
    public void theResponseFieldShouldEqualTrue(String arg0) {

    }

    @When("I create a {string} with following details:")
    public void iCreateABookingWithFollowingDetails(String endpoint,DataTable table) {
        Map<String, String> map = table.asMap(String.class, String.class);

        Booking bookingPayload=new Booking(
                map.get("firstname"),
                map.get("lastname"),
                Integer.parseInt(map.get("totalprice")),
                Boolean.parseBoolean(map.get("depositpaid")),
                new BookingDates(
                        map.get("checkin"),
                        map.get("checkout")
                ),
                map.get("additionalneeds")
        );

        CreateBookingClient bookingClient=new CreateBookingClient();
        Response createBookingResponse = bookingClient.createBooking(endpoint, bookingPayload);
        context.setTestData("statusCode",createBookingResponse.getStatusCode());
        context.response=createBookingResponse;
    }
}
