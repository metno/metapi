# For the load balancer
default['nginx']['default_site_enabled'] = false
default['nginx']['init_style'] = 'upstart'

# For met-api application servers
default['java']['install_flavor'] = 'openjdk'
default['java']['jdk_version'] = '7'
