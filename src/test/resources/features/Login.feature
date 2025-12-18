Feature: Login Feature

  Scenario Outline: Login Test
    Given I open Koel Login Page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I click submit
    Then I am logged in
    Examples:
      | Email | Password |
      | carlitos@testpro.io | vjNWk4Hn |
      | demo@testpro.io | te$t$tudent |
      | test@testpro.io | incorrectpassword |