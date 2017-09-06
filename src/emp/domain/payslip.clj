(ns emp.domain.payslip
  (:require [emp.domain.employee :as employee])
  (:import [emp.domain.employee Employee]
           [java.time Month]))

(def ^:const MONTHS_IN_YEAR 12)

(defprotocol payslip
  (gross-income [this]))

(defrecord MonthPayslip [employee payment_month]
  payslip
  (gross-income
    [this]
    (Math/round (float (/ (:annual_salary employee) MONTHS_IN_YEAR)))))

(defn create
  [employee payment_month]
  {:pre [(instance? Employee employee)
         (instance? Month payment_month)]}
  (->MonthPayslip employee payment_month))
