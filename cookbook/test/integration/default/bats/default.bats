#!/usr/bin/env bats
# vi: se ft=sh:

@test "Java is version 1.7" {
    java -version 2>&1 | {
        run egrep '^java version "1\.7'
        [ $status -eq 0 ]
    }
}

@test "/usr/bin/java points to OpenJDK 7" {
    update-alternatives --query java | {
        run grep "Value: /usr/lib/jvm/java-7-openjdk-amd64/bin/java"
        [ $status -eq 0 ]
    }
}

@test "Upstart job for met-api starts on runlevels 2-5" {
    run grep "start on runlevel \[2345\]" /etc/init/met-api.conf
    [ $status -eq 0 ]
}

@test "Upstart job for met-api respawns the server when dead" {
    run grep "respawn" /etc/init/met-api.conf
    [ $status -eq 0 ]
}
