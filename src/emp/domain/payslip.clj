(ns emp.domain.payslip
  (:require [emp.domain.employee :as employee]
            [clj-uuid :as uuid])
  (:import [emp.domain.employee Employee]
           [java.time Year Month]))

(def ^:const MONTHS_IN_YEAR 12)

(defprotocol Payslip
  (identifier [this])
  (employee-name [this])
  (payment-year [this])
  (payment-month [this])
  (payment-start-day [this])
  (payment-end-day [this])
  (gross-income [this])
  (income-tax [this])
  (net-income [this])
  (super [this]))

(defn- calculate-income-tax
  ([annual_salary]
   (cond
     (< 180000 annual_salary)
     (calculate-income-tax annual_salary 54547 0.45 180000)
     (< 80000 annual_salary)
     (calculate-income-tax annual_salary 17547 0.37 80000)
     (< 37000 annual_salary)
     (calculate-income-tax annual_salary 3572 0.325 37000)
     (< 18200 annual_salary)
     (calculate-income-tax annual_salary 0 0.19 18200)
     :else
     0.0))
  ([annual_salary flat_sum cents_per_dollar dollars_over]
   (double (/ (+ flat_sum
                (* (- annual_salary dollars_over) cents_per_dollar))
             12))))

(defrecord MonthPayslip [identifier employee payment_year payment_month]
  Payslip
  (identifier
    [this]
    identifier)
  (employee-name
    [this]
    (str (get-in employee [:person :first_name])
         " "
         (get-in employee [:person :last_name])))
  (gross-income
    [this]
    (Math/round (double (/ (:annual_salary employee) MONTHS_IN_YEAR))))
  (income-tax
    [this]
    (Math/round (calculate-income-tax (:annual_salary employee))))
  (net-income
    [this]
    (- (.gross-income this) (.income-tax this)))
  (super
    [this]
    (Math/round (* (.gross-income this) (/ (:super_rate employee) 100.0))))
  (payment-year
    [this]
    (str payment_year))
  (payment-month
    [this]
    (str payment_month))
  (payment-start-day
    [this]
    1)
  (payment-end-day
    [this]
    (.length payment_month (.isLeap payment_year))))

(defn create
  [employee payment_year payment_month]
  {:pre [(instance? Employee employee)
         (instance? Year payment_year)
         (instance? Month payment_month)]}
  (->MonthPayslip (uuid/v1) employee payment_year payment_month))
