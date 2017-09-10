(ns emp.domain.payslip
  (:require [emp.domain.employee :as employee]
            [clj-uuid :as uuid])
  (:import [emp.domain.employee Employee]
           [java.time Year Month]))

(def ^:const MONTHS_IN_YEAR 12)

(defprotocol Payslip
  (identifier [this])
  (payment-start-day [this])
  (payment-end-day [this])
  (gross-income [this]))

(defrecord MonthPayslip [identifier employee payment_year payment_month]
  Payslip
  (identifier
    [this]
    identifier)
  (gross-income
    [this]
    (Math/round (float (/ (:annual_salary employee) MONTHS_IN_YEAR))))
  (payment-start-day
    [this]
    1)
  (payment-end-day
    [this]
    (.length payment_month (.isLeap payment_year))))

(defn create
  [employee payment_year payment_month]
  {:pre [(instance? Employee employee)
         (instance? Year payment_year)
         (instance? Month payment_month)]}
  (->MonthPayslip (uuid/v1) employee payment_year payment_month))
