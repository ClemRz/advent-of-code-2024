(ns aoc.day-5.part-1
  (:require [aoc.day-5.common :refer [correctly-ordered? middle scan-document]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))

(defn -main
  []
  (let [{:keys [rules updates]} (scan-document INPUT)]
    (->> (filter (partial correctly-ordered? rules) updates)
         (map middle)
         (reduce +))))

(-main)
; => 4185
