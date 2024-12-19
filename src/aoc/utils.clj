(ns aoc.utils
  (:require [clojure.java.io :as io]))

(defn slurp-input
  []
  (-> *ns* (str ".txt") io/resource slurp))