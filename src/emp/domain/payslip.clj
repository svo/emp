(ns emp.domain.payslip
  (:require [emp.domain.employee :as employee])
  (:import [emp.domain.employee Employee]
           [org.joda.time DateTime]))

(defrecord Payslip [employee payment_start_date])

(defn create
  [employee payment_start_date]
  {:pre [(instance? Employee employee)
         (instance? DateTime payment_start_date)]}
  (->Payslip employee payment_start_date))
