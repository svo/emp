(ns emp.service
  (:require [emp.core :as core]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.interceptor.error :as error-interceptor]
            [ring.util.response :as ring-response]
            [emp.route.handler.payslip :as payslip]
            [environ.core :refer [env]]
            [emp.application.pdf-generator :as pdf-generator]))

(def ^:const DEFAULT_PORT 8080)

(defn- version
  [request]
  (ring-response/response (core/version)))

(defn- payslip-post
  [request]
  (ring-response/created (str "/payslip/" (:id (payslip/post request)))))

(defn- payslip-get
  [request]
  (ring-response/file-response
    (pdf-generator/path (:identifier (:path-params request)))))

(defn port
  []
  (let [environment_port (env :port)]
    (if (clojure.string/blank? environment_port)
      DEFAULT_PORT
      (read-string environment_port))))

(def common-interceptors [(body-params/body-params) http/html-body])

(def exception-interceptor
  (assoc (error-interceptor/error-dispatch
           [context exception]
           [{:exception-type :java.lang.AssertionError}]
           (assoc context :response {:status 400
                                     :body (.getMessage exception)})
           :else
           (assoc context :io.pedestal.interceptor.chain/error exception))
         :name
         ::exception-interceptor))

(def routes #{["/version" :get (conj common-interceptors `version)]
              ["/payslip" :post (conj common-interceptors
                                      exception-interceptor
                                      `payslip-post)]
              ["/payslip/:identifier"
               :get
               (conj common-interceptors
                     exception-interceptor
                     `payslip-get)]})

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/port (port)
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})
