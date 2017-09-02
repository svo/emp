# EMP

* [Trello Board](https://trello.com/b/WoAzNyra/)
* [Continuous Integration](https://app.shippable.com/github/svo/emp/dashboard)

NOTE: this project uses git submodules so you will want to clone recursively to have all expected behaviours.

```
git clone --recursive git@github.com:svo/emp.git
```

## Development environment

Requirements:

1. Ansible (2.2.2.0)
2. Vagrant (1.9.3)
3. VirtualBox (5.1.18 r114002)

To perform provisioning and start using the development environment run:

1. `vagrant up`
2. `vagrant ssh`
3. `cd /vagrant`

## Testing the application

To monitor tests as you edit the source code run the following from the `/vagrant` directory:

1. `lein repl`
2. `(use 'midje.repl)`
3. `(midje.repl/autotest)`

To run tests and generate reports:

`./pre-commit.sh`

## Local Continuous Integration

Navigate to [vagrant-emp-ci.local:8080](http://vagrant-emp-ci.local:8080) if you have `MDNS` support.

## Links

* [Clojure](https://clojure.org)
* [Leiningen](http://leiningen.org)
* [Midje](https://github.com/marick/Midje)