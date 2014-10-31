Feature: Product Information

  Scenario Outline: Products
    Given I ask for the current version of "<product>"
    Then It should reply with "<current version>"

    Examples: Weather API Products
        | product               | current version |
        | helloworld            | 1.0 |

  Scenario Outline: Application is running
    Given I request the "<path>" path
    Then It should respond with "<response>"

    Examples: Products
        | path                                      | response |
        | http://API_HOST_HERE/v1/hello_world       | Hello World |