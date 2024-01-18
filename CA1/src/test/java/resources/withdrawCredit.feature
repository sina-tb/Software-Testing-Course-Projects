Feature: Credit Withdrawal Feature

  Scenario: Withdrawal with Sufficient Credit
    Given a user with credit of 20.0
    When the user withdraws 10.0 of current credit
    Then the new credit should be 10.0

  Scenario: Withdrawal with Insufficient Credit
    Given a user with credit of 20.0
    When the user withdraws 30.0 of current credit
    Then an InsufficientCredit exception should be thrown
