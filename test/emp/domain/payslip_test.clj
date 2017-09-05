(ns emp.domain.payslip-test
  (:require [emp.domain.payslip :as payslip :refer [->Payslip]]
            [emp.domain.employee :refer [map->Employee]])
  (:import [java.util Date])
  (:use [midje.sweet :only [facts fact => throws anything]]
        [cljito.core :only [mock]]))

(facts
  "record"

  (fact
    "should have employee"
    (let [employee (map->Employee {})]
      (:employee (->Payslip employee
                            anything)) => employee)))

(fact
  "should create payslip"
  (let [employee (map->Employee {})
        payment_start_date (mock Date)]
    (payslip/create
      employee
      payment_start_date) => (->Payslip employee
                                        payment_start_date)))

(fact
  "should error if employee is not an Employee"
  (payslip/create ..employee..
                  (mock Date)) => (throws AssertionError))
