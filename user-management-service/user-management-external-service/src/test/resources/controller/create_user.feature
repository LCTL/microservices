Feature: Create User

  Scenario: Create normal user
    Given user with the username 'Lawrence', password '123456' and roles 'ROLE_USER'
    When find by username 'Lawrence'
    Then 'Lawrence' should has been found
    Then '123456' should encoded
    Then roles should contain 'ROLE_USER'