(ns emp.domain.payslip-test
  (:require [emp.domain.payslip :as payslip :refer [->Payslip]]
            [emp.domain.employee :refer [map->Employee]])
  (:use [midje.sweet :only [facts fact => throws]]))

(facts
  "record"

  (fact
    "should have employee"
    (let [employee (map->Employee {})]
      (:employee (->Payslip employee)) => employee)))

(fact
  "should create payslip"
  (let [employee (map->Employee {})]
    (payslip/create
      employee) => (->Payslip employee)))

(fact
  "should error if employee is not an Employee"
  (payslip/create ..employee..) => (throws AssertionError))
