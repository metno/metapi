import time
from urllib2 import Request, urlopen, URLError

# -----------------------------------------------------------------------------
# DOMAIN-MODEL:
# -----------------------------------------------------------------------------
class Product(object):
    def __init__(self, host, version, function):
        self.host                   = host
        self.version                = version
        self.function               = function
        self.url                    = None
        self.the_response_string    = None
        self.timer                  = time.time()        
        self.the_response_time      = None       

        if self.host != ".":
            self.url = self.host
        if self.version != ".":
            self.url += "/" + self.version
        if self.function != ".":
            self.url += "/" + self.function
        
    def get_response_string(self):  
        if self.url != None:           
            req = Request(self.url)
            try:
                response = urlopen(req)
            except URLError as e:
                if hasattr(e, 'reason'):
                    return e.reason
                elif hasattr(e, 'code'):
                    return e.code
            else:
                return response.read()

    def get_response_time(self):
        self.the_response_time = time.time() - self.timer;
        return int(self.the_response_time*1000)