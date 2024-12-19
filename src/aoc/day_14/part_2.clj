(ns aoc.day-14.part-2
  (:require [aoc.day-14.common :refer [predict-robots scan]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))
(defonce SIZE [101 103])
(defonce THRESHOLD 15/100)

(defn -main
  [size input]
  (->> input
       scan
       (predict-robots true size 1 THRESHOLD)))

(time (try (-main SIZE INPUT) (catch Throwable e e)))
; => 6377