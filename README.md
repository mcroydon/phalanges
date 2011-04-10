# phalanges

This is an experimental implementation of the [Finger protocol](http://en.wikipedia.org/wiki/Finger_protocol) as it existed
in 1977 in [RFC 742](http://tools.ietf.org/html/rfc742).

## License

Phalanges is released under the 3-clause BSD license.

## Requirements

Phalanges uses [simple-build-tool](http://code.google.com/p/simple-build-tool/) to manage its external dependencies:

* [Scala](http://www.scala-lang.org/) 2.8.1
* [simple-build-tool](http://code.google.com/p/simple-build-tool/) 0.7.4
* [Netty](http://www.jboss.org/netty) 3.2.4Final
* [Configgy](https://github.com/twitter/configgy) 2.0.2
* [Specs](http://code.google.com/p/specs/) 1.6.7
* [Mockito](http://mockito.org/) 1.8.5
* [Metrics](https://github.com/codahale/metrics) 2.0.0-BETA10

## Configuring and running phalanges

First, update external dependencies and compile:

    $ sbt clean update compile

Then edit and/or copy configuration files to where phalanges expects them to be:

    $ sudo cp config/phalanges.conf /etc
    $ sudo touch /etc/unknown.conf

User information is stored and loaded from `config/user_map.json` by default.

By default, phalanges runs on 127.0.0.1 port 79.  Because of this, you must run phalanges with sudo or as root:

    $ sudo sbt run

### Warning

While there are no known security issues, running experimental software that accepts external network connections
is highly risky.  If you would like to use phalanges on a public interface, consider running it on a high port and
using NAT or a well-tested TCP proxy to expose it to the outside world.

## Querying phalanges

You should now be able to use a finger client to query phalanges:

    $ finger @127.0.0.1
    [127.0.0.1]
    Login                   Name                
    mcroydon                Matt Croydon        
    robot                   A. Robot
    
    $ finger mcroydon@127.0.0.1
    [127.0.0.1]
    mcroydon                Matt Croydon        
    Plan:
    Implement archaic protocols.
    
    $ finger unknown@127.0.0.1
    [127.0.0.1]
    The user unknown was not found.

## Monitoring

Phalanges logs its actions to `logs/phalanges.log` by default.  It also keeps track of the number of indexes and
users served since last reboot.  This information can be found at `http://127.0.0.1:9999/report/` by default.

## Metrics

Phalanges uses [Metrics](https://github.com/codahale/metrics) to instrument usage.  There are currently meters for
index and user requests served::

    4/9/11 4:33:31 PM ==============================================================
    com.postneo.phalanges.FingerHandler:
      indexes:
                 count = 9
             mean rate = 0.30 served/s
         1-minute rate = 0.00 served/s
         5-minute rate = 0.50 served/s
        15-minute rate = 0.80 served/s

      users:
                 count = 3
             mean rate = 0.10 served/s
         1-minute rate = 0.00 served/s
         5-minute rate = 0.08 served/s
        15-minute rate = 0.03 served/s

If you would like to monitor your metrics via JConsole, make sure you use ``sudo jconsole`` so that you're running
JConsole as the same user as your app.

## Testing

You can execute [specs](http://code.google.com/p/specs/) tests with sbt:

    $ sbt clean update test

## To Do

* Make storage backend configurable and create additional storage backends.
* Load user list from disk or datastore.
* Figure out how to use local configs and get rid of `/etc/unknown.conf`.
