(ns emp.application.pdf-generator
  (:use [clj-pdf.core :only [pdf]]))

(def ^:const CURRENCY_SYMBOL "$")
(def ^:const PATH "/var/lib/emp/")
(def ^:const EXTENSION ".pdf")
(def ^:const LABEL_POSTFIX ": ")
(def ^:const EMPLOYEE_LABEL "Employee")
(def ^:const FROM_DATE_LABEL "From Date")
(def ^:const TO_DATE_LABEL "To Date")
(def ^:const GROSS_INCOME_LABEL "Gross Income")
(def ^:const INCOME_TAX_LABEL "Income Tax")
(def ^:const NET_INCOME_LABEL "Net Income")
(def ^:const SUPER_LABEL "Super")

(defn path
  [identifier]
  (str PATH identifier EXTENSION))

(defn- paragraph
  ([label label_postfix value]
   (str label label_postfix value))
  ([label value]
   (paragraph label LABEL_POSTFIX value)))

(defn- currency-paragraph
  [label value]
  (paragraph label (str LABEL_POSTFIX CURRENCY_SYMBOL) value))

(defn generate
  [payslip]
  (pdf
    [{}
     [:paragraph (paragraph EMPLOYEE_LABEL (.employee-name payslip))]
     [:paragraph (str FROM_DATE_LABEL
                      LABEL_POSTFIX
                      (.payment-month payslip)
                      " "
                      (.payment-start-day payslip)
                      " of "
                      (.payment-year payslip))]
     [:paragraph (str TO_DATE_LABEL
                      LABEL_POSTFIX
                      (.payment-month payslip)
                      " "
                      (.payment-end-day payslip)
                      " of "
                      (.payment-year payslip))]
     [:paragraph (currency-paragraph GROSS_INCOME_LABEL
                                     (.gross-income payslip))]
     [:paragraph (currency-paragraph INCOME_TAX_LABEL
                                     (.income-tax payslip))]
     [:paragraph (currency-paragraph NET_INCOME_LABEL
                                     (.net-income payslip))]
     [:paragraph (currency-paragraph SUPER_LABEL
                                     (.super payslip))]]
    (path (.identifier payslip))))
