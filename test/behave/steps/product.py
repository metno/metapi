# -----------------------------------------------------------------------------
# DOMAIN-MODEL:
# -----------------------------------------------------------------------------
class Product(object):
    TRANSFORMATION_MAP = {
        "helloworld"        : "1.0",
        "errornotifications": "1.0",
        "extremeforecast"   : "1.0",
        "extremeswwc"       : "1.2",
        "forestfireindex"   : "1.1",
        "geosattelite"      : "1.3",
        "gribfiles"         : "1.0",
        "icemap"            : "1.0",
        "locationforecast"  : "1.9",
    }
    def __init__(self):
        self.thing  = None
        self.result = None

    @classmethod
    def select_result_for(cls, thing):
        return cls.TRANSFORMATION_MAP.get(thing)

    def add(self, thing):
        self.thing = thing

    def get_current_version(self):
        self.result = self.select_result_for(self.thing)
        #Replace this with the get request to eventual rest api "function getProductInfo for example"