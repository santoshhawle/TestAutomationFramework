package com.bddframework.api.client;

import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class DeleteBookingClient extends APIClient{

    public Response deleteBooking(int bookingId,String endpoint,String token){
        Map<String, Integer> pathParams=new HashMap<>();
        pathParams.put("bookingId",bookingId);
        return spec.cookie("token",token).pathParams(pathParams).delete(endpoint);
    }

}
