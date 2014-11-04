Feature: Product Information

  Scenario Outline: Application is running
    Given I make a get request to "<host>" with version "<version>" and function "<function>" 
    Then It should respond with "<response_string>", in less than "<response_time>" milliseconds
    
    Examples: Products
        | host                         | version   | function           |response_string                                            | response_time   |
        | http://localhost:9000        | .         | .                  |METAPI: Needs Version                                      | 200             |
        | http://localhost:9000        | v1        | .                  |Not Found                                                  | 200             |
        | http://localhost:9000        | v1        | HelloWorld         |Hello World                                                | 200             |
        | http://localhost:9000        | v1        | Observation.:format |observation: :format: 'GET /v1/Observation.:format: List()  | 200             |
        