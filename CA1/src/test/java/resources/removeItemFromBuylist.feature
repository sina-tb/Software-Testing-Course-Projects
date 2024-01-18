Feature: User remove items from buy list

  Scenario: Remove a single item from buy list
    Given a user with following buy list:
      |1        |1        |
    When the user removes product with id "1" form the buy list
    Then the buy list should be:
      |

  Scenario: Remove multiple items of same commodity from buy list
    Given a user with following buy list:
      |2        |3        |
    When the user removes product with id "2" form the buy list
    Then the buy list should be:
      |2        |2        |

  Scenario: Remove an item not present buy list
    Given a user with following buy list:
      |3        |2        |
    When the user removes product with id "2" form the buy list
    Then a CommodityIsNotInButList exception should be thrown

  Scenario: Remove an item with quantity greater 1
    Given a user with following buy list:
      |4        |5        |
    When the user removes product with id "4" form the buy list
    Then the buy list should be:
      |4        |4        |