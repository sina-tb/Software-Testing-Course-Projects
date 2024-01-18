Feature: User's addCredit steps

  Scenario: Add valid amount to user credit (positive credit)
    Given a user with credit of 50.0
    When the user adds 20.0 to their current credit
    Then the new credit should be 70.0

  Scenario: Add invalid amount to user credit (negative credit)
    Given a user with credit of 50.0
    When the user adds -20.0 to their current credit
    Then an InvalidCreditRange exception should be thrown

  Scenario: Add valid amount to user credit (zero amount)
    Given a user with credit of 50.0
    When the user adds 0.0 to their current credit
    Then the new credit should be 50.0