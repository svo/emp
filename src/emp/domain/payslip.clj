(ns emp.domain.payslip
  (:require [emp.domain.employee :as employee])
  (:import [emp.domain.employee Employee]
           [java.time Year Month]))

(def ^:const MONTHS_IN_YEAR 12)

(defprotocol payslip
  (gross-income [this]))

(defrecord MonthPayslip [employee payment_month payment_year]
  payslip
  (gross-income
    [this]
    (Math/round (float (/ (:annual_salary employee) MONTHS_IN_YEAR)))))

(defn create
  [employee payment_month payment_year]
  {:pre [(instance? Employee employee)
         (instance? Month payment_month)
         (instance? Year payment_year)]}
  (->MonthPayslip employee payment_month payment_year))
