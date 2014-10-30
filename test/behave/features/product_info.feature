Feature: Product Information

  Scenario Outline: Products
    Given I specify the "<product>" product name
    When I ask for latest version
    Then It should tell me that "<current version>" is the current version

    Examples: Weather API Products
        | product               | current version |
        | helloworld            | 1.0 |
        | errornotifications    | 1.0 |
        | extremeforecast       | 1.0 |
        | extremeswwc           | 1.2 |
        | forestfireindex       | 1.1 |
        | geosattelite          | 1.3 |
        | gribfiles             | 1.0 |
        | icemap                | 1.0 |
        | locationforecast      | 1.9 |