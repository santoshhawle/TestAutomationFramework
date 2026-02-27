Feature: Restful Booker API - Complete Contract & Field Validation

  As an API automation engineer
  I want to validate all endpoints and field-level responses
  So that the API contract remains stable and correct

  Background:
    Given user get access token

  # -------------------------------------------------------
  # 1. Health Check
  # -------------------------------------------------------
  @api
  Scenario: Verify API health
    When I send a GET request to "/ping"
    Then the response status code should be 201
    And the response body should be empty

    @api
  Scenario:  verify bookings
    When user run the get bookingid get api
    Then verify booking ids are not null

 # -------------------------------------------------------
  # 3. Create Booking
  # -------------------------------------------------------
  @api
  Scenario: Create booking and verify all response fields
    When I create a "/booking" with following details:
      | firstname      | John        |
      | lastname       | Doe         |
      | totalprice     | 120         |
      | depositpaid    | true        |
      | checkin        | 2026-03-01  |
      | checkout       | 2026-03-05  |
      | additionalneeds| Breakfast   |
    Then the response status code should be 200
    And the response should contain field "bookingid"
    And the response should contain object "booking"
    And the response field "booking.firstname" should equal "John"
    And the response field "booking.lastname" should equal "Doe"
    And the response field "booking.totalprice" should equal 120
    And the response field "booking.depositpaid" should equal true
    And the response field "booking.bookingdates.checkin" should equal "2026-03-01"
    And the response field "booking.bookingdates.checkout" should equal "2026-03-05"
    And the response field "booking.additionalneeds" should equal "Breakfast"