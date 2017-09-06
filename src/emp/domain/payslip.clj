(ns emp.domain.payslip
  (:require [emp.domain.employee :as employee])
  (:import [emp.domain.employee Employee]
           [org.joda.time DateTime]))

(def ^:const MONTHS_IN_YEAR 12)

(defprotocol payslip
  (gross-income [this]))

(defrecord MonthPayslip [employee payment_start_date]
  payslip
  (gross-income
    [this]
    (Math/round (float (/ (:annual_salary employee) MONTHS_IN_YEAR)))))

(defn create
  [employee payment_start_date]
  {:pre [(instance? Employee employee)
         (instance? DateTime payment_start_date)]}
  (->MonthPayslip employee payment_start_date))
