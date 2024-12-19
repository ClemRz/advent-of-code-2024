(ns aoc.day-1.part-1
  (:require [aoc.day-1.common :refer [extract-lists]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))

(defn -main
  []
  (->> (extract-lists INPUT)
       (map sort)
       (apply map (comp abs -))
       (reduce +)))

(-main)
; => 1590491