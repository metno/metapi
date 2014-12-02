#
# Cookbook Name:: met-api
# Recipe:: loadbalancer
#
# Copyright (C) 2014 MET Norway
#
# All rights reserved - Do Not Redistribute
#
# This recipe sets up an nginx load balancer.
#

include_recipe 'nginx'

servers = search(:node, "chef_environment:#{node.chef_environment} AND recipe:met-api\\:\\:default",
                 :filter_result => {
                     'ipaddress' => ['ipaddress']
                 })

template '/etc/nginx/sites-available/met-api' do
    source 'nginx.conf.erb'
    owner 'root'
    group 'root'
    mode '0644'
    variables :servers => servers
    notifies :restart, 'service[nginx]'
end

link '/etc/nginx/sites-enabled/met-api' do
    to '/etc/nginx/sites-available/met-api'
    notifies :restart, 'service[nginx]'
end
