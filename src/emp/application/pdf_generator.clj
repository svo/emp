(ns emp.application.pdf-generator
  (:use [clj-pdf.core :only [pdf]]))

(defn path
  [identifier]
  (str identifier ".pdf"))

(defn generate
  [payslip]
  (pdf
    [[:paragraph (str "Start Day: " (.payment-start-day payslip))]
     [:paragraph (str "End Day: " (.payment-end-day payslip))]
     [:paragraph (str "Gross Income: $" (.gross-income payslip))]]
    (path (.identifier payslip))))
