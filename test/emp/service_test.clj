(ns emp.service-test
  (:require [emp.core :as core]
            [io.pedestal.test :refer :all]
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
      (core/version) => ..version..
      (ring-response/response ..version..) => ..result..)))

(facts
  "version route"

  (fact
    "should have expected version"
    (:body (response-for service :get "/version")) => "..version.."
    (provided
      (core/version) => ..version..))

  (fact
    "should have expected headers"
    (let [headers {"Content-Type" "text/html;charset=UTF-8"}]
      (:headers (response-for
                  service
                  :get
                  "/version")) => (contains headers))))
