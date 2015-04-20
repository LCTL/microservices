Feature: Find User

  Scenario: find user by username
    Given the user management system is initialized with the following data using service
      | username | password | strRoles             |
      | Lawrence | 123456   | ROLE_USER,ROLE_ADMIN |
    When find by username 'Lawrence'
    Then 'Lawrence' should has been found
    And password should be hashed format: '123456'
    And should be contains roles: 'ROLE_USER,ROLE_ADMIN'

  Scenario: find user by id
    Given the user management system is initialized with the following data
      | id | username | password | strRoles  |
      | 1  | PP       | 654321   | ROLE_USER |
    When find by id '1'
    Then 'PP' should has been found
    And should be contains roles: 'ROLE_USER'

  Scenario: find user by id
    Given the user management system is initialized with the following data
      | id | username | password | strRoles  |
      | 1  | PP       | 654321   | ROLE_USER |
      | 2  | Lawrence | 654321   | ROLE_USER |
    When find by multiple ids
      | 1 |
      | 2 |
    Then user list should contains id
      | 1 |
      | 2 |