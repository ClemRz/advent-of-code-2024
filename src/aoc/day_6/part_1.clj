(ns aoc.day-6.part-1
  (:require [aoc.day-6.common :refer [go]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))

(defn -main
  []
  (->> INPUT #_"....#.....\n.........#\n..........\n..#.......\n.......#..\n..........\n.#..^.....\n........#.\n#.........\n......#..."
       go
       :visited
       (map drop-last)
       distinct
       count))

(-main)
; => 5129
