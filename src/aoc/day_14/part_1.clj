(ns aoc.day-14.part-1
  (:require [aoc.day-14.common :refer [predict-robots scan freq split safety-factor]]
            [aoc.utils :refer [slurp-input]]
            [clojure.test :refer [is]]))

(defonce INPUT (slurp-input))
(defonce SIZE [101 103])

(defn -main
  [size input]
  (->> input
       scan
       (predict-robots size 100)
       freq
       (split size)
       safety-factor))

(is (= 12 (-main [11 7] "p=0,4 v=3,-3\np=6,3 v=-1,-3\np=10,3 v=-1,2\np=2,0 v=2,-1\np=0,0 v=1,3\np=3,0 v=-2,-2\np=7,6 v=-1,-3\np=3,0 v=-1,-2\np=9,3 v=2,3\np=7,3 v=-1,2\np=2,4 v=2,-3\np=9,5 v=-3,-3")))

(time (try (-main SIZE INPUT) (catch Throwable e e)))
; => 225943500