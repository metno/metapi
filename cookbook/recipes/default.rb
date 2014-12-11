#
# Cookbook Name:: met-api
# Recipe:: default
#
# Copyright (C) 2014 MET Norway
#
# Licensed under the Apache License, Version 2.0.
# See the LICENSE file for further information.
#

include_recipe 'apt'
include_recipe 'java'

template '/etc/init/met-api.conf' do
    source 'upstart/met-api.conf.erb'
    owner 'root'
    group 'root'
    mode '0644'
end

service 'met-api' do
    supports :status => true, :restart => true, :start => true, :stop => true
    provider Chef::Provider::Service::Upstart
    action :nothing
end
