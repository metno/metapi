# For met-api application servers
default['java']['install_flavor'] = 'openjdk'
default['java']['jdk_version'] = '7'

# Application settings
default['met-api']['deploy_directory'] = '/opt/met-api-1.0-SNAPSHOT'

# Auto-generated application settings
default['met-api']['listen_address'] = '0.0.0.0'
default['met-api']['listen_port'] = 9000
default['met-api']['server_binary'] = "#{node['met-api']['deploy_directory']}/bin/met-api"
