(ns emp.domain.person-test
  (:require [emp.domain.person :as person :refer [->Person]])
  (:use [midje.sweet :only [facts fact => anything]]))

(facts
  "record"

  (fact
    "should have first name"
    (:first_name (->Person ..first_name.. anything)) => ..first_name..)

  (fact
    "should have last name"
    (:last_name (->Person anything ..last_name..)) => ..last_name..))
