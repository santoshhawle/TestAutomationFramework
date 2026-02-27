package com.bddframework.api.client;

import com.bddframework.api.payloads.Booking;
import io.restassured.response.Response;

public class CreateBookingClient extends APIClient{

    public Response createBooking(String endpoint, Booking bookingPayload){
        return post(endpoint,bookingPayload);
    }
}
