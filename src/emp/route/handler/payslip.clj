(ns emp.route.handler.payslip
  (:require [ring.util.response :as response]
            [clj-uuid :as uuid]))

(def ^:const ROUTE "/payslip/")

(defn post
  [request]
  (response/created (str ROUTE
                         (uuid/v1))))
