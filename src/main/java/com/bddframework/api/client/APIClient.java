package com.bddframework.api.client;

import com.bddframework.api.config.ConfigLoader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APIClient {

    protected RequestSpecification spec;

    public APIClient() {
            spec= RestAssured.given()
                    .baseUri(ConfigLoader.getBaseUri())
                    .contentType("application/json")
                    .log().all();

    }

    public Response get(String endpoint){
            return spec.when().get(endpoint);
    }

    public Response post(String endpoint, Object payload){
        return spec.when().accept("application/json")
                .body(payload).post(endpoint);
    }

}
