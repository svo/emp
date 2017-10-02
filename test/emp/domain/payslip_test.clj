(ns emp.domain.payslip-test
  (:require [emp.domain.payslip :as payslip :refer [->MonthPayslip]]
            [emp.domain.person :refer [map->Person]]
            [emp.domain.employee :refer [map->Employee]]
            [clj-uuid :as uuid])
  (:import [java.time Year Month])
  (:use [midje.sweet :only [facts fact => throws anything]]))

(facts
  "record"

  (fact
    "should have identifier"
    (.identifier (->MonthPayslip ..identifier..
                                 anything
                                 anything
                                 anything)) => ..identifier..)

  (fact
    "should have employee"
    (:employee (->MonthPayslip anything
                               ..employee..
                               anything
                               anything)) => ..employee..)

  (fact
    "should produce display name"
    (let [person (map->Person {:first_name ..first_name..
                               :last_name ..last_name..})
          employee (map->Employee {:person person})]
      (.employee-name (->MonthPayslip
                        anything
                        employee
                        anything
                        anything)) => "..first_name.. ..last_name.."))

  (fact
    "should have payment month"
    (:payment_month
      (->MonthPayslip anything
                      anything
                      anything
                      ..payment_month..)) => ..payment_month..)

  (fact
    "should have payment year"
    (:payment_year
      (->MonthPayslip anything
                      anything
                      ..payment_year..
                      anything)) => ..payment_year..)

  (fact
    "should get payment start day"
    (.payment-start-day (->MonthPayslip anything
                                        anything
                                        anything
                                        anything)) => 1)

  (fact
    "should get payment year"
    (.payment-year (->MonthPayslip anything
                                   anything
                                   ..year..
                                   anything)) => (str ..year..))

  (fact
    "should get payment month"
    (.payment-month (->MonthPayslip anything
                                    anything
                                    anything
                                    ..month..)) => (str ..month..))

  (fact
    "should get payment end day"
    (.payment-end-day (->MonthPayslip anything
                                      anything
                                      (Year/of 2017)
                                      (Month/FEBRUARY))) => 28)

  (fact
    "should get payment end day for leap year"
    (.payment-end-day (->MonthPayslip anything
                                      anything
                                      (Year/of 2016)
                                      (Month/FEBRUARY))) => 29)

  (fact
    "should handle small gross income"
    (let [employee (map->Employee {:annual_salary 1})]
      (.gross-income (->MonthPayslip anything
                                     employee
                                     anything
                                     anything)) => 0))

  (fact
    "should handle large gross income"
    (let [employee (map->Employee {:annual_salary Integer/MAX_VALUE})]
      (.gross-income (->MonthPayslip anything
                                     employee
                                     anything
                                     anything)) => 178956971))

  (fact
    "should calculate gross income"
    (let [employee (map->Employee {:annual_salary 60000})]
      (.gross-income (->MonthPayslip anything
                                     employee
                                     anything
                                     anything)) => 5000))

  (fact
    "should round down gross income"
    (let [employee (map->Employee {:annual_salary 60050})]
      (.gross-income (->MonthPayslip anything
                                     employee
                                     anything
                                     anything)) => 5004))

  (fact
    "should round up gross income"
    (let [employee (map->Employee {:annual_salary 60174})]
      (.gross-income (->MonthPayslip anything
                                     employee
                                     anything
                                     anything)) => 5015))

  (fact
    "should calculate net income"
    (let [annual_salary 60050
          employee (map->Employee {:annual_salary annual_salary})]
      (.net-income (->MonthPayslip anything
                                   employee
                                   anything
                                   anything)) => 4082
      (provided
        (#'payslip/calculate-income-tax annual_salary) => 921.9375)))

  (fact
    "should calculate super"
    (let [annual_salary 60050
          employee (map->Employee {:annual_salary annual_salary
                                   :super_rate 9})]
      (.super (->MonthPayslip anything
                              employee
                              anything
                              anything)) => 450))

  (fact
    "should calculate super with small salary"
    (let [annual_salary 1
          employee (map->Employee {:annual_salary annual_salary
                                   :super_rate 9})]
      (.super (->MonthPayslip anything
                              employee
                              anything
                              anything)) => 0))

  (fact
    "should handle small income tax"
    (let [income_tax 1
          employee (map->Employee {:annual_salary ..annual_salary..})]
      (.income-tax (->MonthPayslip anything
                                   employee
                                   anything
                                   anything)) => income_tax
      (provided
        (#'payslip/calculate-income-tax
          ..annual_salary..) => (double income_tax))))

  (fact
    "should handle lage income tax"
    (let [income_tax Integer/MAX_VALUE
          employee (map->Employee {:annual_salary ..annual_salary..})]
      (.income-tax (->MonthPayslip anything
                                   employee
                                   anything
                                   anything)) => income_tax
      (provided
        (#'payslip/calculate-income-tax
          ..annual_salary..) => (double income_tax))))

  (fact
    "should have income tax"
    (let [income_tax 5000
          employee (map->Employee {:annual_salary ..annual_salary..})]
      (.income-tax (->MonthPayslip anything
                                   employee
                                   anything
                                   anything)) => income_tax
      (provided
        (#'payslip/calculate-income-tax
          ..annual_salary..) => (double income_tax))))

  (fact
    "should round down income tax"
    (let [income_tax 5001.49
          employee (map->Employee {:annual_salary ..annual_salary..})]
      (.income-tax (->MonthPayslip anything
                                   employee
                                   anything
                                   anything)) => 5001
      (provided
        (#'payslip/calculate-income-tax ..annual_salary..) => income_tax)))

  (fact
    "should round up income tax"
    (let [income_tax 921.9375
          employee (map->Employee {:annual_salary ..annual_salary..})]
      (.income-tax (->MonthPayslip anything
                                   employee
                                   anything
                                   anything)) => 922
      (provided
        (#'payslip/calculate-income-tax ..annual_salary..) => income_tax))))

(facts
  "income tax"

  (fact
    "should be none for 0 - $18,200"
    (#'payslip/calculate-income-tax 18200) => 0.0)

  (fact
    "18,201 - 37,000 19c for each $1 over $18,200"
    (#'payslip/calculate-income-tax 18201) => (/ 0.19 12))

  (fact
    "$37,001 - $80,000 $3,572 plus 32.5c for each $1 over $37,000"
    (#'payslip/calculate-income-tax 60050) => 921.9375)

  (fact
    "$80,001 - $180,000 $17,547 plus 37c for each $1 over $80,000"
    (#'payslip/calculate-income-tax 80001) => (/ 17547.37 12))

  (fact
    "$180,001 and over $54,547 plus 45c for each $1 over $180,000"
    (#'payslip/calculate-income-tax 180001) => (/ 54547.45 12)))

(fact
  "should create payslip"
  (let [employee (map->Employee {})
        payment_year (Year/of 2017)
        payment_month (Month/APRIL)]
    (payslip/create
      employee
      payment_year
      payment_month) => (->MonthPayslip ..identifier..
                                        employee
                                        payment_year
                                        payment_month)
    (provided
      (uuid/v1) => ..identifier..)))

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

(fact
  "should report as invalid date if month before minimum for minimum year"
  (#'payslip/invalid-date? (Year/of 2012)
                           (Month/JUNE)) => true)

(fact
  "should report as valid date if first valid month"
  (#'payslip/invalid-date? (Year/of 2012)
                           (Month/JULY)) => false)

(fact
  "should report as valid date if year after minimum"
  (#'payslip/invalid-date? (Year/of 2013)
                           (Month/JANUARY)) => false)

(fact
  "should report as valid date if year before minimum"
  (#'payslip/invalid-date? (Year/of 2011)
                           (Month/OCTOBER)) => true)

(fact
  "should error if payment date is before July 1st 2012"
  (let [year (Year/of 2012)
        month (Month/JUNE)]
    (payslip/create (map->Employee {})
                    year
                    month) => (throws AssertionError)
    (provided
      (#'payslip/valid-date? year month) => false)))
