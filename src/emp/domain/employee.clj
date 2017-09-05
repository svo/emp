(ns emp.domain.employee
  (:require [emp.domain.person :as person])
  (:import [emp.domain.person Person]))

(defrecord Employee [person annual_salary])

(defn create
  [person annual_salary]
  {:pre [(instance? Person person)
         (integer? annual_salary)
         (pos? annual_salary)]}
  (->Employee person annual_salary))
