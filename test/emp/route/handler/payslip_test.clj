(ns emp.route.handler.payslip-test
  (:require [emp.route.handler.payslip :as payslip]
            [clj-uuid :as uuid]
            [emp.domain.person :as person]
            [emp.domain.employee :as employee]
            [emp.domain.payslip :as payslip-data])
  (:use [midje.sweet :only [facts fact => provided]])
  (:import [java.time Year Month]))

(facts
  "post"

  (fact
    "should create payslip"
    (let [year 2017
          year_of (Year/of year)
          month "APRIL"
          month_of (Month/valueOf month)]
      (payslip/post {:json-params {:first_name ..first_name..
                                   :last_name ..last_name..
                                   :annual_salary ..annual_salary..
                                   :year year
                                   :month month}}) => {:id ..uuid..}
      (provided
        (uuid/v1) => ..uuid..
        (person/create ..first_name..
                       ..last_name..) => ..person..
        (employee/create ..person..
                         ..annual_salary..) => ..employee..
        (payslip-data/create ..employee..
                             year_of
                             month_of) => ..payslip_data..))))
