(ns emp.domain.payslip
  (:require [emp.domain.employee :as employee]
            [clj-uuid :as uuid])
  (:import [emp.domain.employee Employee]
           [java.time Year Month]))

(def ^:const PRECISION 10)
(def ^:const PERCENTAGE_DIVIDER (bigdec 100.0))
(def ^:const MONTHS_IN_YEAR 12)
(def ^:const MINIMUM_YEAR 2012)
(def ^:const DEFAULT_START_DAY 1)
(def ^:const BELOW_FIRST_BRACKET_TAX_RATE 0.0)
(def ^:const FIRST_BRACKET 18200)
(def ^:const FIRST_BRACKET_FLAT_SUM 0)
(def ^:const FIRST_BRACKET_CENTS_PER_DOLLAR (bigdec 0.19))
(def ^:const FIRST_BRACKET_FOR_DOLLARS_OVER 18200)
(def ^:const SECOND_BRACKET 37000)
(def ^:const SECOND_BRACKET_FLAT_SUM 3572)
(def ^:const SECOND_BRACKET_CENTS_PER_DOLLAR (bigdec 0.325))
(def ^:const SECOND_BRACKET_FOR_DOLLARS_OVER 37000)
(def ^:const THIRD_BRACKET 80000)
(def ^:const THIRD_BRACKET_FLAT_SUM 17547)
(def ^:const THIRD_BRACKET_CENTS_PER_DOLLAR (bigdec 0.37))
(def ^:const THIRD_BRACKET_FOR_DOLLARS_OVER 80000)
(def ^:const FOURTH_BRACKET 180000)
(def ^:const FOURTH_BRACKET_FLAT_SUM 54547)
(def ^:const FOURTH_BRACKET_CENTS_PER_DOLLAR (bigdec 0.45))
(def ^:const FOURTH_BRACKET_FOR_DOLLARS_OVER 180000)

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

(defn- invalid-date?
  [year month]
  (or (and (= year (Year/of MINIMUM_YEAR))
           (< (.getValue month) (.getValue (Month/JULY))))
      (.isBefore year (Year/of MINIMUM_YEAR))))

(def valid-date? (complement invalid-date?))

(defn- calculate-income-tax
  ([annual_salary]
   (cond
     (< FOURTH_BRACKET annual_salary)
     (calculate-income-tax annual_salary
                           FOURTH_BRACKET_FLAT_SUM
                           FOURTH_BRACKET_CENTS_PER_DOLLAR
                           FOURTH_BRACKET_FOR_DOLLARS_OVER)
     (< THIRD_BRACKET annual_salary)
     (calculate-income-tax annual_salary
                           THIRD_BRACKET_FLAT_SUM
                           THIRD_BRACKET_CENTS_PER_DOLLAR
                           THIRD_BRACKET_FOR_DOLLARS_OVER)
     (< SECOND_BRACKET annual_salary)
     (calculate-income-tax annual_salary
                           SECOND_BRACKET_FLAT_SUM
                           SECOND_BRACKET_CENTS_PER_DOLLAR
                           SECOND_BRACKET_FOR_DOLLARS_OVER)
     (< FIRST_BRACKET annual_salary)
     (calculate-income-tax annual_salary
                           FIRST_BRACKET_FLAT_SUM
                           FIRST_BRACKET_CENTS_PER_DOLLAR
                           FIRST_BRACKET_FOR_DOLLARS_OVER)
     :else
     BELOW_FIRST_BRACKET_TAX_RATE))
  ([annual_salary flat_sum cents_per_dollar dollars_over]
   (double (with-precision
             PRECISION (/ (+ flat_sum
                             (* (- annual_salary dollars_over)
                                cents_per_dollar))
                          MONTHS_IN_YEAR)))))

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
    (Math/round (double
                  (with-precision PRECISION
                    (* (bigdec (.gross-income this))
                       (/ (bigdec (:super_rate employee))
                          PERCENTAGE_DIVIDER))))))
  (payment-year
    [this]
    (str payment_year))
  (payment-month
    [this]
    (str payment_month))
  (payment-start-day
    [this]
    DEFAULT_START_DAY)
  (payment-end-day
    [this]
    (.length payment_month (.isLeap payment_year))))

(defn create
  [employee payment_year payment_month]
  {:pre [(instance? Employee employee)
         (instance? Year payment_year)
         (instance? Month payment_month)
         (valid-date? payment_year payment_month)]}
  (->MonthPayslip (uuid/v1) employee payment_year payment_month))
