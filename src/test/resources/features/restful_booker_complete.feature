Feature: Restful Booker API - Complete Contract & Field Validation

  As an API automation engineer
  I want to validate all endpoints and field-level responses
  So that the API contract remains stable and correct

  Background:
    Given the base URI is "https://restful-booker.herokuapp.com"
    And the request content type is "application/json"

  # -------------------------------------------------------
  # 1. Health Check
  # -------------------------------------------------------

  Scenario: Verify API health
    When I send a GET request to "/ping"
    Then the response status code should be 201
    And the response body should be empty

  # -------------------------------------------------------
  # 2. Authentication
  # -------------------------------------------------------

  Scenario: Generate authentication token with valid credentials
    When I send a POST request to "/auth" with body:
      """
      {
        "username": "admin",
        "password": "password123"
      }
      """
    Then the response status code should be 200
    And the response should contain field "token"
    And the field "token" should not be null

  Scenario: Authentication with invalid credentials
    When I send a POST request to "/auth" with body:
      """
      {
        "username": "admin",
        "password": "wrongpassword"
      }
      """
    Then the response status code should be 200
    And the response body should contain:
      | reason | Bad credentials |

  # -------------------------------------------------------
  # 3. Create Booking
  # -------------------------------------------------------

  Scenario: Create booking and verify all response fields
    When I send a POST request to "/booking" with body:
      """
      {
        "firstname": "John",
        "lastname": "Doe",
        "totalprice": 120,
        "depositpaid": true,
        "bookingdates": {
          "checkin": "2026-03-01",
          "checkout": "2026-03-05"
        },
        "additionalneeds": "Breakfast"
      }
      """
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

  # -------------------------------------------------------
  # 4. Get All Bookings
  # -------------------------------------------------------

  Scenario: Retrieve all booking IDs
    When I send a GET request to "/booking"
    Then the response status code should be 200
    And the response should be an array
    And each element should contain field "bookingid"

  # -------------------------------------------------------
  # 5. Get Booking by ID
  # -------------------------------------------------------

  Scenario: Retrieve booking by valid ID
    Given a booking exists
    When I send a GET request to "/booking/{bookingId}"
    Then the response status code should be 200
    And the response should contain fields:
      | firstname       |
      | lastname        |
      | totalprice      |
      | depositpaid     |
      | bookingdates    |
    And the response field "bookingdates.checkin" should not be null
    And the response field "bookingdates.checkout" should not be null

  Scenario: Retrieve booking with invalid ID
    When I send a GET request to "/booking/99999999"
    Then the response status code should be 404

  # -------------------------------------------------------
  # 6. Query Parameter Filters
  # -------------------------------------------------------

  Scenario: Retrieve booking by firstname and lastname filter
    When I send a GET request to "/booking?firstname=John&lastname=Doe"
    Then the response status code should be 200
    And the response should be an array
    And each element should contain field "bookingid"

  Scenario: Retrieve booking by date range
    When I send a GET request to "/booking?checkin=2026-03-01&checkout=2026-03-10"
    Then the response status code should be 200

  # -------------------------------------------------------
  # 7. Update Booking (PUT)
  # -------------------------------------------------------

  Scenario: Full update booking and verify changed fields
    Given a booking exists
    And I generate authentication token
    When I send a PUT request to "/booking/{bookingId}" with body:
      """
      {
        "firstname": "Jane",
        "lastname": "Smith",
        "totalprice": 300,
        "depositpaid": false,
        "bookingdates": {
          "checkin": "2026-04-01",
          "checkout": "2026-04-10"
        },
        "additionalneeds": "Lunch"
      }
      """
    Then the response status code should be 200
    And the response field "firstname" should equal "Jane"
    And the response field "lastname" should equal "Smith"
    And the response field "totalprice" should equal 300
    And the response field "depositpaid" should equal false
    And the response field "bookingdates.checkin" should equal "2026-04-01"
    And the response field "bookingdates.checkout" should equal "2026-04-10"
    And the response field "additionalneeds" should equal "Lunch"

  # -------------------------------------------------------
  # 8. Partial Update (PATCH)
  # -------------------------------------------------------

  Scenario: Partial update booking and verify only modified fields changed
    Given a booking exists
    And I generate authentication token
    When I send a PATCH request to "/booking/{bookingId}" with body:
      """
      {
        "firstname": "UpdatedName",
        "additionalneeds": "Dinner"
      }
      """
    Then the response status code should be 200
    And the response field "firstname" should equal "UpdatedName"
    And the response field "additionalneeds" should equal "Dinner"

  # -------------------------------------------------------
  # 9. Delete Booking
  # -------------------------------------------------------

  Scenario: Delete booking and verify removal
    Given a booking exists
    And I generate authentication token
    When I send a DELETE request to "/booking/{bookingId}"
    Then the response status code should be 201

    When I send a GET request to "/booking/{bookingId}"
    Then the response status code should be 404