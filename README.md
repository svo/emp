# EMP

* [Trello Board](https://trello.com/b/WoAzNyra/)
* [Continuous Integration](https://app.shippable.com/github/svo/emp/dashboard)
* [API Contracts](https://github.com/svo/emp-contract)
* [Browser Client](https://github.com/svo/emp-browser)

__NOTE:__ this project uses git submodules so you will want to clone recursively to have all expected behaviours.

```
git clone --recursive git@github.com:svo/emp.git
```

## Decisions

* using [Clojure](https://clojure.org/)
	* used on most recent project so using here for familiarity
	* structs seem sufficient i.e. no strict requirement for objects
* implementing behaviour over `HTTP` to support browser `UI` to demonstrate competency
* using `HTTP` over message bus for simplicity

## Assumptions

* incremental delivery to allow for feedback and demonstrate work patterns and processes is acceptable
* annual salary(positive integer) is dollar figure i.e. no cents
* reading of "payment start date" in requirements reflects desire to be able to add date other than first of the month
* "But feel free to use any format you want" in requirements line "Here is the csv input and output format we provide. (But feel free to use any format you want)" allows for `HTTP` with browser `UI` delivery

## Development environment

Requirements:

1. [Ansible (2.2.2.0)](https://www.ansible.com/)
2. [Vagrant (1.9.3)](https://www.vagrantup.com/)
3. [VirtualBox (5.1.18 r114002)](https://www.virtualbox.org/)

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

## Running the application

1. `vagrant up`
2. `vagrant ssh`
3. `cd /vagrant`
3. `lein run-dev`

Navigate to [vagrant-emp.local:8080](http://vagrant-emp.local:8080/version) if you have `MDNS` support.

## Local Continuous Integration

Navigate to [vagrant-emp-ci.local:8080](http://vagrant-emp-ci.local:8080) if you have `MDNS` support.

## Design

### Domain Model
![domain model](https://github.com/svo/emp/blob/master/doc/domain-model.png?raw=true)

### API Contracts

* [Api Contracts HTML](http://htmlpreview.github.io/?https://github.com/svo/emp-contract/blob/master/api.html)

## Links

* [Clojure](https://clojure.org)
* [Leiningen](http://leiningen.org)
* [Midje](https://github.com/marick/Midje)
* [Pedestal](https://github.com/pedestal/pedestal)
