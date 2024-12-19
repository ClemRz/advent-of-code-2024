(ns aoc.day-2.common
  (:require [clojure.string :as str]))

(def ^:private parse-report (comp (partial map parse-long) #(str/split % #"\s+")))

(defn extract-reports
  [input]
  (->> (str/split-lines input)
       (map parse-report)))

(defn- gradual?
  [a b]
  (<= 1 (abs (- a b)) 3))

(defn all-gradual?
  [report]
  (->> (map gradual? (drop-last report) (drop 1 report))
       (every? identity)))
