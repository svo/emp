(ns emp.domain.employee-test
  (:require [emp.domain.employee :as employee :refer [->Employee]]
            [emp.domain.person :refer [map->Person]])
  (:use [midje.sweet :only [facts fact => throws anything provided]]))

(facts
  "record"

  (fact
    "should have person"
    (:person (->Employee ..person..
                         anything
                         anything)) => ..person..)

  (fact
    "should have annual salary"
    (:annual_salary (->Employee
                      anything
                      ..annual_salary..
                      anything)) => ..annual_salary..)

  (fact
    "should have annual salary"
    (let [person (map->Person {})]
      (:super_rate (->Employee
                     person
                     anything
                     ..super_rate..)) => ..super_rate..)))

(fact
  "should get employee"
  (let [person (map->Person {})
        annual_salary 1]
    (employee/create
      person
      annual_salary
      ..super_rate..) => (->Employee person
                                     annual_salary
                                     ..super_rate..)))

(fact
  "should error if person is not a Person"
  (employee/create ..person..
                   1
                   ..super_rate..) => (throws AssertionError))

(fact
  "should be a valid annual salary"
  (#'employee/valid-annual-salary? 1) => true)

(fact
  "should not be a valid annual salary if not an integer"
  (#'employee/valid-annual-salary? ..coconuts..) => false)

(fact
  "should not be a valid annual salary if not positive integer"
  (#'employee/valid-annual-salary? -1) => false)

(fact
  "should report 0 as a valid super rate"
  (#'employee/valid-super-rate? 0) => true)

(fact
  "should report 50 as a valid super rate"
  (#'employee/valid-super-rate? 50) => true)

(fact
  "should report 51 as an invalid super rate"
  (#'employee/valid-super-rate? 51) => false)

(fact
  "should report -1 as an invalid super rate"
  (#'employee/valid-super-rate? -1) => false)

(fact
  "should report string as an invalid super rate"
  (#'employee/valid-super-rate? "") => false)

(fact
  "should error if annual salary is not valid"
  (employee/create (map->Person {})
                   ..annual_salary..
                   ..super_rate..) => (throws AssertionError)
  (provided
    (#'employee/valid-annual-salary? ..annual_salary..) => false))
