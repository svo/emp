(ns emp.route.handler.payslip-test
  (:require [emp.route.handler.payslip :as payslip]
            [ring.util.response :as response]
            [clj-uuid :as uuid])
  (:use [midje.sweet :only [facts fact => provided]]))

(facts
  "post"

  (fact
    "should return created resource location"
    (payslip/post ..request..) => ..response..
    (provided
      (uuid/v1) => ..uuid..
      (response/created "/payslip/..uuid..") => ..response..)))
