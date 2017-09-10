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
    (payment-start-day [this] ..start_day..)
    (payment-end-day [this] ..end_day..)
    (gross-income [this] ..gross_income..))

  (fact
    "should get generated"
    (let [start_day_line (str "Start Day: " ..start_day..)
          end_day_line (str "End Day: " ..end_day..)
          gross_income_line (str "Gross Income: $" ..gross_income..)]
      (generator/generate (->TestPayslip)) => ..result..
      (provided
        (generator/path ..identifier..) => ..path..
        (pdf
          [[:paragraph start_day_line]
           [:paragraph end_day_line]
           [:paragraph gross_income_line]]
          ..path..) => ..result..))))
