(ns aoc.utils
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn slurp-input
  []
  (-> *ns* (str ".txt") io/resource slurp))

(defn char-vector->char-matrix
  [width char-vector]
  (->> (partition width char-vector)
       (map str/join)
       (str/join "\n")))