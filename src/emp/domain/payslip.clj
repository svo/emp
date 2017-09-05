(ns emp.domain.payslip
  (:require [emp.domain.employee :as employee])
  (:import [emp.domain.employee Employee]))

(defrecord Payslip [employee payment_start_date])

(defn create
  [employee payment_start_date]
  {:pre [(instance? Employee employee)]}
  (->Payslip employee payment_start_date))
