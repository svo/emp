(ns emp.application.pdf-generator
  (:use [clj-pdf.core :only [pdf]]))

(def ^:const CURRENCY_SYMBOL "$")
(def ^:const PATH "/var/lib/emp/")

(defn path
  [identifier]
  (str PATH identifier ".pdf"))

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
     [:paragraph (str "Gross Income: "
                      CURRENCY_SYMBOL
                      (.gross-income payslip))]
     [:paragraph (str "Income Tax: "
                      CURRENCY_SYMBOL
                      (.income-tax payslip))]
     [:paragraph (str "Net Income: "
                      CURRENCY_SYMBOL
                      (.net-income payslip))]
     [:paragraph (str "Super: "
                      CURRENCY_SYMBOL
                      (.super payslip))] ]
    (path (.identifier payslip))))
