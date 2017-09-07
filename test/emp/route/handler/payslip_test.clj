(ns emp.route.handler.payslip-test
  (:require [emp.route.handler.payslip :as payslip]
            [clj-uuid :as uuid])
  (:use [midje.sweet :only [facts fact => provided]]))

(facts
  "post"

  (fact
    "should return created payslip identifier"
    (payslip/post ..request..) => {:id ..uuid..}
    (provided
      (uuid/v1) => ..uuid..)))
