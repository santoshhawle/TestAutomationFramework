package com.bddframework.stepdefinition;

import com.bddframework.api.auth.AuthUtil;
import com.bddframework.api.client.*;
import com.bddframework.api.payloads.Booking;
import com.bddframework.api.payloads.BookingDates;
import com.bddframework.api.payloads.PartialBooking;
import com.bddframework.api.utils.DataTableUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;



public class ApiStepdefs {

    private static final Logger logger =
            LoggerFactory.getLogger(ApiStepdefs.class);

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
        Object bookingObject =context.response.jsonPath().get("booking");
        Assert.assertNotNull(bookingObject);
    }

    @And("the response field {string} should equal {string}")
    public void theResponseFieldShouldEqual(String key, String expectedValue) {
        String actualValue = context.response.jsonPath().get(key);
        Assert.assertEquals(actualValue,expectedValue);
    }

    @And("the response field {string} should equal {int}")
    public void theResponseFieldShouldEqual(String key, int expectedValue) {
        int actualValue = context.response.jsonPath().get(key);
        Assert.assertEquals(actualValue,expectedValue);
    }

    @And("the response field {string} should equal true")
    public void theResponseFieldShouldEqualTrue(String key) {
        boolean actualValue = context.response.jsonPath().get(key);
        Assert.assertTrue(actualValue);
    }

    @When("I create a {string} with following details:")
    public void iCreateABookingWithFollowingDetails(String endpoint,DataTable table) {
        Booking bookingPayload= DataTableUtils.getBookingPayload(table);
        CreateBookingClient bookingClient=new CreateBookingClient();
        Response createBookingResponse = bookingClient.createBooking(endpoint, bookingPayload);
        context.setTestData("statusCode",createBookingResponse.getStatusCode());
        context.response=createBookingResponse;
    }

    @When("I send a PUT request to {string} with body:")
    public void iSendAPUTRequestToWithBody(String endpoint,DataTable table) {
        Booking bookingPayload= DataTableUtils.getBookingPayload(table);
        String token = context.getTestData("token");
        UpdateBookingClient updateBookingClient=new UpdateBookingClient();
        Response response = updateBookingClient.updateBooking(context.getTestData("bookingid"),endpoint,bookingPayload,token);
        context.setTestData("statusCode",response.getStatusCode());
        context.response=response;
    }

    @And("the response field {string} should equal false")
    public void theResponseFieldShouldEqualFalse(String key) {
        boolean actualValue = context.response.jsonPath().get(key);
        Assert.assertFalse(actualValue);
    }

    @When("I create a {string} with following details and store booking id:")
    public void iCreateAWithFollowingDetailsAndStoreBookingId(String endpoint, DataTable table) {
        Booking bookingPayload= DataTableUtils.getBookingPayload(table);
        CreateBookingClient bookingClient=new CreateBookingClient();
        Response createBookingResponse = bookingClient.createBooking(endpoint, bookingPayload);
        context.setTestData("bookingid",createBookingResponse.jsonPath().get("bookingid"));
    }

    @When("I send a PATCH request to {string} with body:")
    public void iSendAPATCHRequestToWithBody(String endpoint, DataTable table) {
        PartialBooking partialBookingPayload = DataTableUtils.getPartialBookingPayload(table);
        UpdateBookingClient updateBookingClient=new UpdateBookingClient();
        String token = context.getTestData("token");
        Response response = updateBookingClient.partialBookingUpdate(context.getTestData("bookingid"), endpoint, partialBookingPayload, token);
        context.response=response;
        context.setTestData("statusCode",response.getStatusCode());
    }

    @When("I send a DELETE request to {string}")
    public void iSendADELETERequestTo(String endpoint) {
        DeleteBookingClient deleteBookingClient=new DeleteBookingClient();
        String token = context.getTestData("token");
        Response response = deleteBookingClient.deleteBooking(context.getTestData("bookingid"),endpoint, token);
        context.response=response;
        context.setTestData("statusCode",response.getStatusCode());
        logger.info("Status Code is "+ response.getStatusCode());
    }

    @When("I send a GET request with bookingId to {string}")
    public void iSendAGETRequestWithBookingIdTo(String endpoint) {
        APIClient apiClient=new APIClient();
        int bookingid = context.getTestData("bookingid");
        Response response = apiClient.get(endpoint,bookingid);
        context.response=response;
        context.setTestData("statusCode",response.getStatusCode());
    }
}
