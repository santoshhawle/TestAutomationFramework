@ui
Feature: Verify login feature

  Background:
    Given user launch the application

    @login
  Scenario: Verify valid login feature
    When user login to application with valid credentials
    Then verify user is successfully logged in
