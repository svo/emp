(ns emp.domain.person)

(defrecord Person [first_name last_name])

(defn create
  [first_name last_name]
  {:pre [(instance? java.lang.String first_name)
         (instance? java.lang.String last_name)]}
  (->Person first_name last_name))
