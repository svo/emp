(ns emp.application.pdf-generator
  (:use [clj-pdf.core :only [pdf]]))

(defn path
  [identifier]
  (str "/var/lib/emp/" identifier ".pdf"))

(defn generate
  [payslip]
  (pdf
    [{}
     [:paragraph (str "Employee: " (.employee-name payslip))]
     [:paragraph (str "From Date: "
                      (.payment-month payslip)
                      " "
                      (.payment-start-day payslip)
                      " of "
                      (.payment-year payslip))]
     [:paragraph (str "To Date: "
                      (.payment-month payslip)
                      " "
                      (.payment-end-day payslip)
                      " of "
                      (.payment-year payslip))]
     [:paragraph (str "Gross Income: $" (.gross-income payslip))]
     [:paragraph (str "Income Tax: $" (.income-tax payslip))]
     [:paragraph (str "Net Income: $" (.net-income payslip))]
     [:paragraph (str "Super: $" (.super payslip))] ]
    (path (.identifier payslip))))
