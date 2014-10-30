from behave   import given, when, then
from hamcrest import assert_that, equal_to
from product import Product

# ----------------------------------------------------------------------------
# STEPS:
# ----------------------------------------------------------------------------
@given(u'I specify the "{product}" product name')
def step_request_information_about_product(context, product):
    context.product = Product()
    context.product.add(product)

@when(u'I ask for latest version')
def step_ask_for_latest_version(context):
    context.product.get_current_version()

@then(u'It should tell me that "{current_version}" is the current version')
def step_tell_me_version(context, current_version):
    assert_that(context.product.result, equal_to(current_version))
