(ns aoc.day-8.part-2
  (:require [aoc.day-8.common :refer [anti-nodes-v2 compute scan]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))

(defn -main
  []
  (->> INPUT #_"............\n........0...\n.....0......\n.......0....\n....0.......\n......A.....\n............\n............\n........A...\n.........A..\n............\n............"
       scan
       (compute anti-nodes-v2)
       count))

(-main)
; => 1019
