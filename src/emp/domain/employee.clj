(ns emp.domain.employee
  (:import [emp.domain.person Person]))

(defrecord Employee [person])

(defn create
  [person]
  {:pre [(instance? Person person)]}
  (->Employee person))
