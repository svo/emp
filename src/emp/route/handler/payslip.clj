(ns emp.route.handler.payslip
  (:require [ring.util.response :as response]
            [clj-uuid :as uuid]))

(defn post
  [request]
  {:id (uuid/v1)})
