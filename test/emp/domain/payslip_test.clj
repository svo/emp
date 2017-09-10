(ns emp.domain.payslip-test
  (:require [emp.domain.payslip :as payslip :refer [->MonthPayslip]]
            [emp.domain.employee :refer [map->Employee]])
  (:import [java.time Year Month])
  (:use [midje.sweet :only [facts fact => throws anything]]))

(facts
  "record"

  (fact
    "should have employee"
    (:employee (->MonthPayslip ..employee..
                               anything
                               anything)) => ..employee..)

  (fact
    "should have payment month"
    (:payment_month
      (->MonthPayslip anything
                      anything
                      ..payment_month..)) => ..payment_month..)

  (fact
    "should have payment year"
    (:payment_year
      (->MonthPayslip anything
                      ..payment_year..
                      anything)) => ..payment_year..)

  (fact
    "should calculate gross income"
    (let [employee (map->Employee {:annual_salary 60000})]
      (.gross-income (->MonthPayslip employee
                                     anything
                                     anything)) => 5000))

  (fact
    "should round down gross income"
    (let [employee (map->Employee {:annual_salary 60050})]
      (.gross-income (->MonthPayslip employee
                                     anything
                                     anything)) => 5004))

  (fact
    "should round up gross income"
    (let [employee (map->Employee {:annual_salary 60174})]
      (.gross-income (->MonthPayslip employee
                                     anything
                                     anything)) => 5015)))

(fact
  "should create payslip"
  (let [employee (map->Employee {})
        payment_year (Year/of 2017)
        payment_month (Month/APRIL)]
    (payslip/create
      employee
      payment_year
      payment_month) => (->MonthPayslip employee
                                        payment_year
                                        payment_month)))

(fact
  "should error if employee is not an Employee"
  (payslip/create ..coconuts..
                  (Year/of 2017)
                  (Month/APRIL)) => (throws AssertionError))

(fact
  "should error if payment month is not a Month"
  (payslip/create (map->Employee {})
                  (Year/of 2017)
                  ..coconuts..) => (throws AssertionError))

(fact
  "should error if payment year is not a Year"
  (payslip/create (map->Employee {})
                  ..coconuts..
                  (Month/APRIL)) => (throws AssertionError))
