(ns emp.service
  (:require [emp.core :as core]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-response]))

(defn- version
  [request]
  (ring-response/response (core/version)))

(def common-interceptors [(body-params/body-params) http/html-body])

(def routes #{["/version" :get (conj common-interceptors `version)]})

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})
