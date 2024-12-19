(ns aoc.day-12.part-1
  (:require [aoc.day-12.common :refer [build-regions compute-cost measure-areas measure-perimeters scan]]
            [aoc.utils :refer [slurp-input]]
            [clojure.test :refer [is]]))

(defonce INPUT (slurp-input))

(defn -main
  [input]
  (-> input
      scan
      build-regions
      measure-areas
      measure-perimeters
      compute-cost))

(is (= 140 (-main "AAAA\nBBCD\nBBCC\nEEEC")))
(is (= 772 (-main "OOOOO\nOXOXO\nOOOOO\nOXOXO\nOOOOO")))
(is (= 1930 (-main "RRRRIICCFF\nRRRRIICCCF\nVVRRRCCFFF\nVVRCCCJFFF\nVVVVCJJCFE\nVVIVCCJJEE\nVVIIICJJEE\nMIIIIIJJEE\nMIIISIJEEE\nMMMISSJEEE")))

(time (try (-main INPUT) (catch Throwable e e)))
; => 1415378