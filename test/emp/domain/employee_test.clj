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
  (let [person (map->Person {})
        annual_salary 1]
    (employee/create
      person
      annual_salary) => (->Employee person
                                    annual_salary)))

(fact
  "should error if person is not a Person"
  (employee/create ..person..
                   1) => (throws AssertionError))

(fact
  "should error if annual salary is not an integer"
  (employee/create (map->Person {})
                   ..annual_salary..) => (throws AssertionError))

(fact
  "should error if annual salary is not positive"
  (employee/create (map->Person {})
                   -1) => (throws AssertionError))
