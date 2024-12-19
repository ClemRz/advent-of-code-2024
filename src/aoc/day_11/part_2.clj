(ns aoc.day-11.part-2
  (:require [aoc.day-11.common :refer [blink scan]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))

(defn -main
  [input]
  (->> input
       scan
       (blink 75)
       count))

;39 18216ms => 67,485,927
(time (-main INPUT))
; => Saturates resources. Couldn't get a response. Needs a more optimized strategy. Maybe by storing a dictionary in an EDN file?
