package com.bddframework.api.client;

import io.restassured.response.Response;

public class GetBookingClient extends APIClient{

    public Response getBookingIds(){
        return get("/booking");
    }

    public Response getBooking(int id){
        return get("booking/"+id);
    }

}
