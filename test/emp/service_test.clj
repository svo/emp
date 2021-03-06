(ns emp.service-test
  (:require [emp.core :as core]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [io.pedestal.http :as http-server]
            [emp.service :as service]
            [emp.server :as server]
            [ring.util.response :as ring-response]
            [emp.route.handler.payslip :as payslip]
            [emp.application.pdf-generator :as pdf-generator]
            [environ.core :as environ])
  (:use [midje.sweet :only [facts
                            fact
                            =>
                            contains
                            anything
                            against-background
                            =throws=>]])
  (:import [guru.nidi.ramltester RamlLoaders]
           [org.apache.http.client.methods HttpPost HttpGet]
           [org.apache.http.entity StringEntity]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(defn- has-interceptor
  [route method interceptor_name]
  (some #(and (= (first %) route)
              (= (second %) method)
              (some (fn [interceptor]
                      (= (:name interceptor)
                         interceptor_name))
                    (nth % 2)))
        service/routes))

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

  (facts
    "post method"

    (fact
      "should handle request"
      (let [headers {"Location" "/payslip/..id.."}]
        (:headers (response-for
                    service
                    :post
                    "/payslip")) => (contains headers)
        (provided
          (payslip/post anything) => {:id ..id..})))

    (fact
      "should have exception interceptor"
      (has-interceptor
        "/payslip"
        :post
        ::service/exception-interceptor) => true)

    (fact
      "should process bad request"
      (:status (response-for
                 service
                 :post
                 "/payslip")) => 400
      (provided
        (payslip/post anything) =throws=> (AssertionError.)))

    (fact
      "should process internal server error"
      (:status (response-for
                 service
                 :post
                 "/payslip")) => 500
      (provided
        (payslip/post anything) =throws=> (Exception.))))

  (facts
    "get method"

    (fact
      "should handle request"
      (:status
        (response-for
          service
          :get
          "/payslip/..identifier..")) => 200
      (provided
        (pdf-generator/path "..identifier..") => ..path..
        (ring-response/file-response ..path..) => {:body ""
                                                   :headers {}
                                                   :status 200}))

    (fact
      "should have exception interceptor"
      (has-interceptor
        "/payslip/:identifier"
        :get
        ::service/exception-interceptor) => true)

    (fact
      "should process bad request"
      (:status (response-for
                 service
                 :get
                 "/payslip/..identifier..")) => 400
      (provided
        (pdf-generator/path "..identifier..") =throws=> (AssertionError.)))

    (fact
      "should process internal server error"
      (:status (response-for
                 service
                 :get
                 "/payslip/..identifier..")) => 500
      (provided
        (pdf-generator/path "..identifier..") =throws=> (Exception.)))))

(facts
  :contracts
  "contracts"

  (against-background
    [(before :contents (future (http-server/start server/runnable-service)))
     (after :contents (http-server/stop server/runnable-service))]

    (def id (atom ""))

    (fact
      "should match POST request contract"
      (let [api (.load (RamlLoaders/fromGithub "svo" "emp-contract") "api.raml")
            http_client (.createHttpClient api)
            http_post (HttpPost. (str "http://localhost:"
                                      (service/port)
                                      "/payslip"))
            entity (StringEntity. "{\"first_name\": \"Sean\",
                                  \"last_name\": \"Van Osselaer\",
                                  \"annual_salary\": 175000,
                                  \"super_rate\": 9,
                                  \"year\": 2017,
                                  \"month\": \"APRIL\"}")]
        (.setEntity http_post entity)
        (.setHeader http_post "Content-type" "application/json")
        (reset! id (last (clojure.string/split
                           (.getValue
                             (first (.getHeaders
                                      (.execute http_client
                                                http_post) "Location"))) #"/")))
        (.isEmpty (.getLastReport http_client)) => true))

    (fact
      "should match GET request contract"
      (let [api (.load (RamlLoaders/fromGithub "svo" "emp-contract") "api.raml")
            http_client (.createHttpClient api)
            http_get (HttpGet. (str "http://localhost:"
                                    (service/port)
                                    "/payslip/"
                                    @id))]
        (.execute http_client http_get)
        (.isEmpty (.getLastReport http_client)) => true))))
