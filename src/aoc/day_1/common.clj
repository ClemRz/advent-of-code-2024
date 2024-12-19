(ns aoc.day-1.common
  (:require [clojure.string :as str]))

(def ^:private parse-pair (comp (partial map parse-long) rest (partial re-matches #"(\d+)\s+(\d+)")))

(defn extract-lists
  [input]
  (->> (str/split-lines input)
       (reduce
         (fn [acc s]
           (map conj acc (parse-pair s)))
         [[] []])))