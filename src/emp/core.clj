(ns emp.core
  (:require [trptcolin.versioneer.core :as versioning]))

(defn version
  []
  (versioning/get-version
    "is.qual"
    "emp"))
