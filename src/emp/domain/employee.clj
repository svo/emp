(ns emp.domain.employee
  (:require [emp.domain.person :as person])
  (:import [emp.domain.person Person]))

(defrecord Employee [person annual_salary super_rate])

(defn- valid-annual-salary?
  [annual_salary]
  (and (integer? annual_salary)
       (pos? annual_salary)))

(defn create
  [person annual_salary super_rate]
  {:pre [(instance? Person person)
         (valid-annual-salary? annual_salary)]}
  (->Employee person annual_salary super_rate))
