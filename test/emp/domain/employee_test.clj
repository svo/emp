(ns emp.domain.employee-test
  (:require [emp.domain.employee :as employee :refer [->Employee]])
  (:use [midje.sweet :only [facts fact =>]]))

(facts
  "record"

  (fact
    "should have person"
    (:person (->Employee ..person..)) => ..person..))

(fact
  "should get employee"
  (employee/create ..person..) => (->Employee ..person..))
