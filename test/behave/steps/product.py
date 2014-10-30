import urllib2

# -----------------------------------------------------------------------------
# DOMAIN-MODEL:
# -----------------------------------------------------------------------------
class Product(object):
    VERSION_MAP = {
        "helloworld":"1.0"
    }
    def __init__(self):
        self.thing  = None
        self.result = None

    @classmethod
    def select_version_for(cls, thing):
        return cls.VERSION_MAP.get(thing)

    def add(self, thing):
        self.thing = thing

    def get_current_version(self):
        self.result = self.select_version_for(self.thing)
        #Replace this with the get request to eventual rest api "function getProductInfo for example"

    def get_response_string(self):
        response = urllib2.urlopen(self.thing)
        html = response.read()
        self.result = html
        #Replace this with the get request to eventual rest api "function getProductInfo for example"