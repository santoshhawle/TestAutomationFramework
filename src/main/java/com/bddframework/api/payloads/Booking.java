package com.bddframework.api.payloads;

public record Booking(
        String firstname,
                       String lastname,
                       int totalprice,
                       boolean depositpaid,
                       BookingDates bookingdates,
                       String additionalneeds)
{
}
