(ns emp.domain.person-test
  (:require [emp.domain.person :as person :refer [->Person]])
  (:use [midje.sweet :only [facts fact =>]]))

(facts
  "record"

  (fact
    "should have first name"
    (:first_name (->Person ..first_name..)) => ..first_name..))
