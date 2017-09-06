(ns emp.domain.payslip-test
  (:require [emp.domain.payslip :as payslip :refer [->MonthPayslip]]
            [emp.domain.employee :refer [map->Employee]])
  (:import [java.time Month])
  (:use [midje.sweet :only [facts fact => throws anything]]))

(facts
  "record"

  (fact
    "should have employee"
    (let [employee (map->Employee {})]
      (:employee (->MonthPayslip employee
                                 anything)) => employee))

  (fact
    "should have payment month"
    (let [payment_month (Month/APRIL)]
      (:payment_month
        (->MonthPayslip anything
                        payment_month)) => payment_month))
  (fact
    "should calculate gross income"
    (let [employee (map->Employee {:annual_salary 60000})]
      (.gross-income (->MonthPayslip employee
                                     anything)) => 5000))

  (fact
    "should round down gross income"
    (let [employee (map->Employee {:annual_salary 60050})]
      (.gross-income (->MonthPayslip employee
                                     anything)) => 5004))

  (fact
    "should round up gross income"
    (let [employee (map->Employee {:annual_salary 60174})]
      (.gross-income (->MonthPayslip employee
                                     anything)) => 5015)))

(fact
  "should create payslip"
  (let [employee (map->Employee {})
        payment_month (Month/APRIL)]
    (payslip/create
      employee
      payment_month) => (->MonthPayslip employee
                                        payment_month)))

(fact
  "should error if employee is not an Employee"
  (payslip/create ..employee..
                  (Month/APRIL)) => (throws AssertionError))

(fact
  "should error if payment month is not a Month"
  (payslip/create (map->Employee {})
                  ..coconuts..) => (throws AssertionError))
