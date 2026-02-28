package com.bddframework.api.utils;

import com.bddframework.api.payloads.Booking;
import com.bddframework.api.payloads.BookingDates;

import java.util.Map;

public class MapToPojoUtils {

    private MapToPojoUtils(){

    }

    public static Booking getBookingPayload(Map<String,String> map){
        Booking bookingPayload=new Booking(
                map.get("firstname"),
                map.get("lastname"),
                (int)Double.parseDouble(map.get("totalprice")),
                Boolean.parseBoolean(map.get("depositpaid")),
                new BookingDates(
                        map.get("checkin"),
                        map.get("checkout")
                ),
                map.get("additionalneeds")
        );
        return bookingPayload;
    }

}
