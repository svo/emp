(ns emp.route.handler.payslip
  (:require [ring.util.response :as response]
            [clj-uuid :as uuid]
            [emp.domain.person :as person]
            [emp.domain.employee :as employee]
            [emp.domain.payslip :as payslip-data])
  (:import [java.time Year Month]))

(defn post
  [request]
  (let [json (:json-params request)
        person (person/create (:first_name json)
                              (:last_name json))
        employee (employee/create person (:annual_salary json))
        payslip_data (payslip-data/create employee
                                          (Year/of (:year json))
                                          (Month/valueOf(:month json)))]
    {:id (uuid/v1)}))
