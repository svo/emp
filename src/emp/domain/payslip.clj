(ns emp.domain.payslip
  (:require [emp.domain.employee :as employee])
  (:import [emp.domain.employee Employee]
           [org.joda.time DateTime]))

(def ^:const MONTHS_IN_YEAR 12)

(defprotocol PayslipCalculator
  (gross-income [this]))

(defrecord Payslip [employee payment_start_date]
  PayslipCalculator
  (gross-income
    [this]
    (/ (bigint (:annual_salary employee))
       MONTHS_IN_YEAR)))

(defn create
  [employee payment_start_date]
  {:pre [(instance? Employee employee)
         (instance? DateTime payment_start_date)]}
  (->Payslip employee payment_start_date))
