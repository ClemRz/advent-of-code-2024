(ns aoc.day-2.part-1
  (:require [aoc.day-2.common :refer [extract-reports all-gradual?]]
            [aoc.utils :refer [slurp-input]]
            [clojure.test :refer [is]]))

(defonce INPUT (slurp-input))

(defn safe?
  [report]
  (and (or (apply > report)
           (apply < report))
       (all-gradual? report)))

(is (safe? [7 6 4 2 1]))
(is (not (safe? [1 2 7 8 9])))
(is (not (safe? [9 7 6 2 1])))
(is (not (safe? [1 3 2 4 5])))
(is (not (safe? [8 6 4 4 1])))
(is (safe? [1 3 6 7 9]))

(defn -main
  []
  (->> (extract-reports INPUT)
       (map safe?)
       (filter true?)
       count))

(-main)
; => 680