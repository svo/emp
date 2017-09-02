(ns emp.service-test
  (:require [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [emp.service :as service]
            [ring.util.response :as ring-response])
  (:use [midje.sweet :only [facts fact => contains]]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(facts
  "handlers"

  (fact
    "should return version"
    (#'service/version ..request..) => ..result..
    (provided
      (ring-response/response "0.0.1-SNAPSHOT") => ..result..)))

(facts
  "version route"

  (fact
    "should have expected version"
    (:body (response-for service :get "/version")) => "0.0.1-SNAPSHOT")

  (fact
    "should have expected headers"
    (let [headers {"Content-Type" "text/html;charset=UTF-8"}]
      (:headers (response-for
                  service
                  :get
                  "/version")) => (contains headers))))
