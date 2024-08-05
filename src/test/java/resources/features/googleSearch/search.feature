Feature: As a user
  I want to run a google search

    @complete
  Scenario: Go to google search page
    Given I am on the google search page
    Then I can see the search box

    @complete
  Scenario: Run a google search
    Given I am on the google search page
    And I run a search
    Then I search results are returned