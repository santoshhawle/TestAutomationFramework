package com.bddframework.api.filters;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class ResponseSpecLogFilter implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);

        log.info("========= API RESPONSE =========");
        log.info("Status Code: {}", response.getStatusCode());
        log.info("Headers: {}", response.getHeaders());
        log.info("Body: {}", response.getBody().asString());

        return response;
    }
}
