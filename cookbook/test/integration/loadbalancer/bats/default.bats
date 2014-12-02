#!/usr/bin/env bats
# vi: se ft=sh:

#set -x

@test "nginx is installed" {
    run which nginx
    [ $status -eq 0 ]
}

@test "nginx is proxying to a gateway" {
    curl -i http://`hostname -f` | {
        run egrep "502 Bad Gateway"
    }
}
