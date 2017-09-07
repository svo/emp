(ns emp.service
  (:require [emp.core :as core]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-response]
            [emp.route.handler.payslip :as payslip]))

(defn- version
  [request]
  (ring-response/response (core/version)))

(defn- payslip-post
  [request]
  (ring-response/created (str "/payslip/" (:id (payslip/post request)))))

(def common-interceptors [(body-params/body-params) http/html-body])

(def routes #{["/version" :get (conj common-interceptors `version)]
              ["/payslip" :post (conj common-interceptors `payslip-post)]})

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})
