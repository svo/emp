(ns emp.domain.payslip-test
  (:require [emp.domain.payslip :as payslip :refer [->MonthPayslip]]
            [emp.domain.employee :refer [map->Employee]]
            [clj-time.core :as date-time])
  (:import [org.joda.time DateTime])
  (:use [midje.sweet :only [facts fact => throws anything]]))

(facts
  "record"

  (fact
    "should have employee"
    (let [employee (map->Employee {})]
      (:employee (->MonthPayslip employee
                                 anything)) => employee))

  (fact
    "should have payment start date"
    (let [payment_start_date (date-time/now)]
      (:payment_start_date
        (->MonthPayslip anything
                        payment_start_date)) => payment_start_date))
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
        payment_start_date (date-time/now)]
    (payslip/create
      employee
      payment_start_date) => (->MonthPayslip employee
                                             payment_start_date)))

(fact
  "should error if employee is not an Employee"
  (payslip/create ..employee..
                  (date-time/now)) => (throws AssertionError))

(fact
  "should error if payment start date is not a DateTime"
  (payslip/create (map->Employee {})
                  ..date_time..) => (throws AssertionError))
