(ns emp.domain.employee)

(defrecord Employee [person])

(defn create
  [person]
  (->Employee person))
