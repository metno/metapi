from behave import *
from hamcrest import assert_that, equal_to, is_not

@given('Behave is installed')
def step_impl(context):
    pass

@when('we implement a test')
def step_impl(context):
    assert True is not False

@then('it will be tested')
def step_impl(context):
    assert context.failed is False
