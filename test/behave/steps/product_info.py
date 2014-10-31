from behave   import given, when, then
from hamcrest import assert_that, equal_to
from product import Product

# ----------------------------------------------------------------------------
# STEPS:
# ----------------------------------------------------------------------------
@given(u'I ask for the current version of "{product}"')
def step_request_information_about_product(context, product):
    context.product = Product()
    context.product.add(product)

@then(u'It should reply with "{current_version}"')
def step_tell_me_version(context, current_version):
    assert_that(context.product.result, equal_to(context.product.get_current_version()))

@given(u'I request the "{path}" path')
def step_request_path(context, path):
    context.product = Product()
    context.product.add(path)

@then(u'It should respond with "{response}"')
def step_return_result(context, response):
    assert_that(context.product.result, equal_to(context.product.get_response_string()))