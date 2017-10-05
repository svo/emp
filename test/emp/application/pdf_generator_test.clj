(ns emp.application.pdf-generator-test
  (:require [emp.application.pdf-generator :as generator]
            [emp.domain.payslip :as payslip :refer :all]
            [midje.open-protocols :refer [defrecord-openly]])
  (:use [midje.sweet :only [facts fact => provided]]
        [clj-pdf.core :only [pdf]]))

(fact
  "should generate path"
  (generator/path ..identifier..) => "/var/lib/emp/..identifier...pdf")

(fact
  "should generate paragraph"
  (#'generator/paragraph
    ..label..
    ..value..) => "..label..: ..value..")

(fact
  "should generate paragraph with label postfix"
  (#'generator/paragraph
    ..label..
    ..label_postfix..
    ..value..) => "..label....label_postfix....value..")

(fact
  "should generate currency paragraph"
  (#'generator/currency-paragraph
    ..label..
    ..value..) => "..label..: $..value..")

(facts
  "PDF"

  (defrecord-openly TestPayslip []
    payslip/Payslip
    (identifier [this] ..identifier..)
    (employee-name [this] ..employee..)
    (payment-year [this] ..year..)
    (payment-month [this] ..month..)
    (payment-start-day [this] ..start_day..)
    (payment-end-day [this] ..end_day..)
    (gross-income [this] ..gross_income..)
    (income-tax [this] ..income_tax..)
    (net-income [this] ..net_income..)
    (super [this] ..super..))

  (fact
    "should get generated"
    (let [employee_line ..employee_line..
          date_line (str "From Date: "
                         ..month..
                         " "
                         ..start_day..
                         " of "
                         ..year..)
          end_day_line (str "To Date: "
                            ..month..
                            " "
                            ..end_day..
                            " of "
                            ..year..)
          gross_income_line ..gross_income_line..
          income_tax_line ..income_tax_line..
          net_income_line ..net_income_line..
          super_line ..super_line..]
      (generator/generate (->TestPayslip)) => ..result..
      (provided
        (#'generator/paragraph generator/EMPLOYEE_LABEL
                               ..employee..) => ..employee_line..
        (#'generator/currency-paragraph
          generator/GROSS_INCOME_LABEL
          ..gross_income..) => ..gross_income_line..
        (#'generator/currency-paragraph
          generator/INCOME_TAX_LABEL
          ..income_tax..) => income_tax_line
        (#'generator/currency-paragraph
          generator/NET_INCOME_LABEL
          ..net_income..) => net_income_line
        (#'generator/currency-paragraph
          generator/SUPER_LABEL
          ..super..) => super_line
        (generator/path ..identifier..) => ..path..
        (pdf
          [{}
           [:paragraph employee_line]
           [:paragraph date_line]
           [:paragraph end_day_line]
           [:paragraph gross_income_line]
           [:paragraph income_tax_line]
           [:paragraph net_income_line]
           [:paragraph super_line]]
          ..path..) => ..result..))))
