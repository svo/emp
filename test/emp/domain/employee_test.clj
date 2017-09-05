(ns emp.domain.employee-test
  (:require [emp.domain.employee :as employee :refer [->Employee]]
            [emp.domain.person :refer [map->Person]])
  (:use [midje.sweet :only [facts fact => throws]]))

(facts
  "record"

  (fact
    "should have person"
    (let [person (map->Person {})]
      (:person (->Employee person)) => person)))

(fact
  "should get employee"
  (let [person (map->Person {})]
    (employee/create person) => (->Employee person)))

(fact
  "should error if person is not a Person"
  (employee/create ..person..) => (throws AssertionError))
