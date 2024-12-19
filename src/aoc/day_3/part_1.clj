(ns aoc.day-3.part-1
  (:require [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))

(defn extract-mults
  [memory]
  (re-seq #"mul\((\d+),(\d+)\)" memory))

(defn -main
  []
  (->> INPUT
       extract-mults
       (map (comp (partial map parse-long) rest))
       (reduce (fn [acc v] (+ acc (apply * v))) 0)))

(-main)
; => 175015740
