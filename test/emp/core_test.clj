(ns emp.core-test
  (:require [emp.core :as core]
            [trptcolin.versioneer.core :as versioning])
  (:use [midje.sweet :only [fact =>]]))

(fact
  "should return version details"
  (core/version) => ..version..
  (provided
    (versioning/get-version "is.qual"
                            "emp") => ..version..))
