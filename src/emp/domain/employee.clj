(ns emp.domain.employee
  (:require [emp.domain.person :as person])
  (:import [emp.domain.person Person]))

(def ^:const MIN_SUPER_RATE 0)
(def ^:const MAX_SUPER_RATE 50)

(defrecord Employee [person annual_salary super_rate])

(defn- valid-annual-salary?
  [annual_salary]
  (and (integer? annual_salary)
       (pos? annual_salary)))

(defn- valid-super-rate?
  [super_rate]
  (and (integer? super_rate)
       (<= super_rate MAX_SUPER_RATE)
       (>= super_rate MIN_SUPER_RATE)))

(defn create
  [person annual_salary super_rate]
  {:pre [(instance? Person person)
         (valid-annual-salary? annual_salary)
         (valid-super-rate? super_rate)]}
  (->Employee person annual_salary super_rate))
