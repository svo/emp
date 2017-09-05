(ns emp.domain.payslip
  (:require [emp.domain.employee :as employee])
  (:import [emp.domain.employee Employee]))

(defrecord Payslip [employee])

(defn create
  [employee]
  {:pre [(instance? Employee employee)]}
  (->Payslip employee))
