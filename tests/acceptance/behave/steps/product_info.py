from behave   import given, when, then
from hamcrest import assert_that, equal_to, close_to, less_than_or_equal_to, greater_than
from product import Product

# ----------------------------------------------------------------------------
# STEPS:
# ----------------------------------------------------
@given(u'I make a get request to "{host}" with version "{version}" and function "{function}"')
def step_get_request(context, host, version, function):
    context.product = Product(host, version, function)

@then(u'It should respond with "{response_string}", in less than "{response_time}" milliseconds')
def step_response_string(context, response_string, response_time):
    assert_that(response_string,equal_to(context.product.get_response_string()))
    assert_that(int(context.product.get_response_time()), less_than_or_equal_to(int(response_time)))