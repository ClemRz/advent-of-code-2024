(ns aoc.day-12.part-2
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
      #_compute-cost))

#_(is (= 80 (-main "AAAA\nBBCD\nBBCC\nEEEC")))
#_(is (= 436 (-main "OOOOO\nOXOXO\nOOOOO\nOXOXO\nOOOOO")))
#_(is (= 236 (-main "EEEEE\nEXXXX\nEEEEE\nEXXXX\nEEEEE")))
#_(is (= 368 (-main "AAAAAA\nAAABBA\nAAABBA\nABBAAA\nABBAAA\nAAAAAA")))
#_(is (= 1206 (-main "RRRRIICCFF\nRRRRIICCCF\nVVRRRCCFFF\nVVRCCCJFFF\nVVVVCJJCFE\nVVIVCCJJEE\nVVIIICJJEE\nMIIIIIJJEE\nMIIISIJEEE\nMMMISSJEEE")))

#_(time (try (-main INPUT) (catch Throwable e e)))
; =>