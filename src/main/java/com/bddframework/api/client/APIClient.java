package com.bddframework.api.client;

import com.bddframework.api.config.ConfigLoader;
import com.bddframework.api.exception.APIException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class APIClient {
    private static final Logger logger =
            LoggerFactory.getLogger(APIClient.class);
    protected RequestSpecification spec;

    public APIClient() {
        spec = RestAssured.given()
                .baseUri(ConfigLoader.getBaseUri())
                .contentType("application/json");

    }

    public Response get(String endpoint) {
        Response response = null;
        try {
            response=spec.when().get(endpoint);
        } catch (Exception e) {
            logger.error("Get request failed for endpoint:" + endpoint + ":" + e.getCause());
        }
        return response ;
    }

    public Response get(String endpoint, int bookingId) {
        Map<String, Integer> pathParams = new HashMap<>();
        pathParams.put("bookingId", bookingId);
        return spec.when().pathParams(pathParams).get(endpoint);
    }

    public Response post(String endpoint, Object payload) {
        return spec.when().accept("application/json")
                .body(payload).post(endpoint);
    }

}
