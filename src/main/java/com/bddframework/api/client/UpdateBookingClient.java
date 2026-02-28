package com.bddframework.api.client;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class UpdateBookingClient extends APIClient{

    public Response updateBooking(int bookingId,String endpoint,Object body,String token){
        Map<String,Integer> pathParams=new HashMap<>();
        pathParams.put("bookingId",bookingId);
        return spec.accept("application/json").cookie("token",token).pathParams(pathParams).body(body).put(endpoint);
    }

    public Response partialBookingUpdate(int bookingId, String endpoint, Object body, String token){
        Map<String,Integer> pathParams=new HashMap<>();
        pathParams.put("bookingId",bookingId);
        return spec.accept("application/json").cookie("token",token).pathParams(pathParams).body(body).patch(endpoint);
    }
}
