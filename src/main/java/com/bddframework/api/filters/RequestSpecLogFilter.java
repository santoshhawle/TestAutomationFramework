package com.bddframework.api.filters;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class RequestSpecLogFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        log.info("========= API REQUEST =========");
        log.info("URI: {}", requestSpec.getURI());
        log.info("Method: {}", requestSpec.getMethod());
        log.info("Headers: {}", requestSpec.getHeaders());
        log.info("Body: {}", Optional.ofNullable(requestSpec.getBody()));
        Response response = ctx.next(requestSpec, responseSpec);
        return response;
    }
}
