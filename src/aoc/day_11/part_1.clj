(ns aoc.day-11.part-1
  (:require [aoc.day-11.common :refer [blink scan]]
            [aoc.utils :refer [slurp-input]]
            [clojure.test :refer [is]]))

(defonce INPUT (slurp-input))

(defn -main
  [input]
  (->> input
       scan
       (blink 25)
       count))

(is (= 55312 (-main "125 17")))

; 70ms
(time (-main INPUT))
; => 194557
