(ns aoc.day-8.part-1
  (:require [aoc.day-8.common :refer [anti-nodes-v1 compute scan]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))

(defn -main
  []
  (->> INPUT #_"............\n........0...\n.....0......\n.......0....\n....0.......\n......A.....\n............\n............\n........A...\n.........A..\n............\n............"
       scan
       (compute anti-nodes-v1)
       count))

(-main)
; => 301
