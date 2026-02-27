Feature: Verify booking

  Background:
    Given user get access token

    @api
  Scenario:  verify bookings
    When user run the get bookingid get api
    Then verify booking ids are not null