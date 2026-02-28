package com.bddframework.api.utils;

import com.bddframework.api.payloads.Booking;
import com.bddframework.api.payloads.BookingDates;
import com.bddframework.api.payloads.PartialBooking;
import io.cucumber.datatable.DataTable;

import java.util.Map;

public class DataTableUtils {

    private DataTableUtils(){

    }

    public static Booking getBookingPayload(DataTable table){
        Map<String, String> map = table.asMap(String.class, String.class);
        Booking bookingPayload=new Booking(
                map.get("firstname"),
                map.get("lastname"),
                Integer.parseInt(map.get("totalprice")),
                Boolean.parseBoolean(map.get("depositpaid")),
                new BookingDates(
                        map.get("checkin"),
                        map.get("checkout")
                ),
                map.get("additionalneeds")
        );
        return bookingPayload;
    }

    public static PartialBooking getPartialBookingPayload(DataTable table){
        Map<String, String> map = table.asMap(String.class, String.class);
        PartialBooking partialBookingPayload=new PartialBooking(
                map.get("firstname"),
                map.get("additionalneeds")
        );
        return partialBookingPayload;
    }
}
