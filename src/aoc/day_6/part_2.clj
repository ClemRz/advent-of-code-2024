(ns aoc.day-6.part-2
  (:require [aoc.day-6.common :refer [go]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))

(defn -main
  []
  (->> INPUT #_"....#.....\n.........#\n..........\n..#.......\n.......#..\n..........\n.#..^.....\n........#.\n#.........\n......#..."
       (go true)
       :loops
       (map drop-last)
       distinct
       count))

(-main)
; => 1919 too high, 737 too low, not 1820, not 1819 <= COULDN'T FIND THE SOLUTION

