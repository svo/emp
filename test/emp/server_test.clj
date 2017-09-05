(ns emp.server-test
  (:require [emp.server :as server]
            [io.pedestal.http :as http-server])
  (:use [midje.sweet :only [facts fact => provided anything]]))

(facts

  (with-redefs [server/runnable-service ..runnable_service..]

    (fact
      "should start service for lein run"
      (server/-main anything) => ..result..
      (provided
        (http-server/start ..runnable_service..) => ..result..))))
