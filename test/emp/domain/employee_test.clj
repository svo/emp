(ns emp.domain.employee-test
  (:require [emp.domain.employee :as employee :refer [->Employee]]
            [emp.domain.person :refer [map->Person]])
  (:use [midje.sweet :only [facts fact => throws anything]]))

(facts
  "record"

  (fact
    "should have person"
    (let [person (map->Person {})]
      (:person (->Employee person
                           anything)) => person))

  (fact
    "should have annual salary"
    (let [person (map->Person {})]
      (:annual_salary (->Employee
                        person
                        ..annual_salary..)) => ..annual_salary..)))

(fact
  "should get employee"
  (let [person (map->Person {})]
    (employee/create
      person
      ..annual_salary..) => (->Employee person
                                        ..annual_salary..)))

(fact
  "should error if person is not a Person"
  (employee/create ..person..
                   ..annual_salary..) => (throws AssertionError))
