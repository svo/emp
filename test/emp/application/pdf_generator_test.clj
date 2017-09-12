(ns emp.application.pdf-generator-test
  (:require [emp.application.pdf-generator :as generator]
            [emp.domain.payslip :as payslip :refer :all]
            [midje.open-protocols :refer [defrecord-openly]])
  (:use [midje.sweet :only [facts fact => provided]]
        [clj-pdf.core :only [pdf]]))

(fact
  "should generate path"
  (generator/path ..identifier..) => "..identifier...pdf")

(facts
  "PDF"

  (defrecord-openly TestPayslip []
    payslip/Payslip
    (identifier [this] ..identifier..)
    (employee-name [this] ..employee..)
    (payment-start-day [this] ..start_day..)
    (payment-end-day [this] ..end_day..)
    (gross-income [this] ..gross_income..)
    (income-tax [this] ..income_tax..)
    (net-income [this] ..net_income..)
    (super [this] ..super..))

  (fact
    "should get generated"
    (let [employee_line (str "Employee: " ..employee..)
          start_day_line (str "Start Day: " ..start_day..)
          end_day_line (str "End Day: " ..end_day..)
          gross_income_line (str "Gross Income: $" ..gross_income..)
          income_tax_line (str "Income Tax: $" ..income_tax..)
          net_income_line (str "Net Income: $" ..net_income..)
          super_line (str "Super: $" ..super..)]
      (generator/generate (->TestPayslip)) => ..result..
      (provided
        (generator/path ..identifier..) => ..path..
        (pdf
          [{}
           [:paragraph employee_line]
           [:paragraph start_day_line]
           [:paragraph end_day_line]
           [:paragraph gross_income_line]
           [:paragraph income_tax_line]
           [:paragraph net_income_line]
           [:paragraph super_line]]
          ..path..) => ..result..))))
