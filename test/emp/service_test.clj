(ns emp.service-test
  (:require [emp.core :as core]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [io.pedestal.http :as http-server]
            [emp.service :as service]
            [emp.server :as server]
            [ring.util.response :as ring-response]
            [emp.route.handler.payslip :as payslip]
            [environ.core :as environ])
  (:use [midje.sweet :only [facts
                            fact
                            =>
                            contains
                            anything
                            against-background]])
  (:import [guru.nidi.ramltester RamlLoaders]
           [org.apache.http.client.methods HttpPost]
           [org.apache.http.entity StringEntity]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(facts
  "should default to 8080 port"

  (fact
    "when environment variable absent"
    (service/port) => 8080
    (provided
      (environ/env :port) => nil))

  (fact
    "when environment variable empty string"
    (service/port) => 8080
    (provided
      (environ/env :port) => "")))

(fact
  "should use environment variable port when provided"
  (service/port) => 8081
  (provided
    (environ/env :port) => "8081"))

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

(facts
  "payslip route"

  (fact
    "should handle post request"
    (let [headers {"Location" "/payslip/..id.."}]
      (:headers (response-for
                  service
                  :post
                  "/payslip")) => (contains headers)
      (provided
        (payslip/post anything) => {:id ..id..}))))

(facts
  :contracts
  "contracts"

  (against-background
    [(before :contents (future (http-server/start server/runnable-service)))
     (after :contents (http-server/stop server/runnable-service))]

    (fact
      "should match post request contract"
      (let [api (.load (RamlLoaders/fromGithub "svo" "emp-contract") "api.raml")
            http_client (.createHttpClient api)
            http_post (HttpPost. (str "http://localhost:"
                                      (service/port)
                                      "/payslip"))
            entity (StringEntity. "{\"first_name\": \"Sean\",
                                  \"last_name\": \"Van Osselaer\",
                                  \"annual_salary\": 175000,
                                  \"year\": 2017,
                                  \"month\": \"APRIL\"}")]
        (.setEntity http_post entity)
        (.setHeader http_post "Content-type" "application/json")
        (.execute http_client http_post)
        (.isEmpty (.getLastReport http_client)) => true))))
