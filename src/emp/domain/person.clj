(ns emp.domain.person)

(defrecord Person [first_name last_name])

(defn create
  [first_name last_name]
  (->Person first_name last_name))
