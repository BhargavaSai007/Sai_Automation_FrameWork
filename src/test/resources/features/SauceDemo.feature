Feature: SauceDemo Application - Login & Shopping

  Background:
    Given User is on SauceDemo website

  # Login Feature Scenarios
  Scenario: Valid user can login with correct credentials
    When User logs in with username "standard_user" and password "secret_sauce"
    Then User should be successfully logged in
    And User should see products page

  Scenario: User cannot login with invalid credentials
    When User logs in with username "invalid_user" and password "wrong_password"
    Then Login should fail
    And Error message should be displayed with text "Username and password do not match"

  Scenario: Locked user cannot login
    When User logs in with username "locked_out_user" and password "secret_sauce"
    Then Login should fail
    And Error message should be displayed with text "locked out"

  # Shopping Feature Scenario
  Scenario: User can add product to cart and verify
    When User logs in with username "standard_user" and password "secret_sauce"
    And User is on products page
    And User adds "Backpack" product to cart
    Then Cart should show 1 item
    When User navigates to shopping cart
    Then Cart page should display
    And Cart should contain "Backpack" product

