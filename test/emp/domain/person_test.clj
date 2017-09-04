(ns emp.domain.person-test
  (:require [emp.domain.person :as person :refer [->Person]])
  (:use [midje.sweet :only [facts fact => anything throws]]))

(facts
  "record"

  (fact
    "should have first name"
    (:first_name (->Person "..first_name.." anything)) => "..first_name..")

  (fact
    "should have last name"
    (:last_name (->Person "anything" ..last_name..)) => ..last_name..))

(fact
  "should get person"
  (person/create "..first_name.."
                 ..last_name..) => (->Person "..first_name.."
                                             ..last_name..))

(fact
  "should error if first name is not a string"
  (person/create ..first_name.. anything) => (throws AssertionError))
