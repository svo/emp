(ns emp.application.pdf-generator
  (:use [clj-pdf.core :only [pdf]]))

(defn path
  [identifier]
  (str identifier ".pdf"))

(defn generate
  [payslip]
  (pdf
    [{}
     [:paragraph (str "Employee: " (.employee-name payslip))]
     [:paragraph (str "Year: " (.payment-year payslip))]
     [:paragraph (str "Month: " (.payment-month payslip))]
     [:paragraph (str "Start Day: " (.payment-start-day payslip))]
     [:paragraph (str "End Day: " (.payment-end-day payslip))]
     [:paragraph (str "Gross Income: $" (.gross-income payslip))]
     [:paragraph (str "Income Tax: $" (.income-tax payslip))]
     [:paragraph (str "Net Income: $" (.net-income payslip))]
     [:paragraph (str "Super: $" (.super payslip))] ]
    (path (.identifier payslip))))
