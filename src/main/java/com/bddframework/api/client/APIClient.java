package com.bddframework.api.client;

import com.bddframework.api.config.ConfigLoader;
import com.bddframework.api.filters.RequestSpecLogFilter;
import com.bddframework.api.filters.ResponseSpecLogFilter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class APIClient {
    protected RequestSpecification spec;



    public APIClient() {
        spec = RestAssured.given()
                .baseUri(ConfigLoader.getBaseUri())
                .contentType("application/json");

        if (isRequestLoggingEnabled()) {
            spec.filter(new RequestSpecLogFilter());
        }else if (isResponseLoggingEnabled()) {
            spec.filter(new ResponseSpecLogFilter());
        }
    }

    private static boolean isRequestLoggingEnabled(){
            return Boolean.valueOf(ConfigLoader.getProperty("requestLog"));
    }

    private static boolean isResponseLoggingEnabled(){
        return Boolean.valueOf(ConfigLoader.getProperty("responseLog"));
    }

    public Response get(String endpoint) {
        Response response = null;
        try {
            response=spec.when().get(endpoint);
        } catch (Exception e) {
            log.error("Get request failed for endpoint:" + endpoint + ":" + e.getCause());
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
