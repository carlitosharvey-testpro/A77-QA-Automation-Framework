Feature: Login feature

  Scenario: Positive Scenario
    Given I open Login page
    When I enter email "carlitos@testpro.io"
    And I enter password "vjNWk4Hn"
    And I submit
    Then I am logged in

  Scenario: Negative Scenario
    Given I open Login page
    When I enter email "carlitos@testpro.io"
    And I enter password "incorrect"
    And I submit
    Then I am not logged in