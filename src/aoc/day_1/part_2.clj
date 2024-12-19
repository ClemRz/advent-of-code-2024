(ns aoc.day-1.part-2
  (:require [aoc.day-1.common :refer [extract-lists]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))

(defn -main
  []
  (let [[l m] (-> (extract-lists INPUT)
                  vec
                  (update 1 frequencies))]
    (reduce
      (fn [acc n]
        (+ acc (* n (get m n 0))))
      0
      l)))

(-main)
; => 22588371