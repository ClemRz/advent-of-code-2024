(ns aoc.day-18.part-1
  (:require [aoc.day-18.common :refer [dijkstra scan init]]
            [aoc.utils :refer [slurp-input]]
            [clojure.test :refer [is]]))

(defonce INPUT (slurp-input))

(defn -main
  [size b input]
  (->> input
       (scan size)
       (init b)
       dijkstra
       :cost))

(is (= 22 (-main [7 7] 12 "5,4\n4,2\n4,5\n3,0\n2,1\n6,3\n2,4\n1,5\n0,6\n3,3\n2,6\n5,1\n1,2\n5,5\n2,5\n6,5\n1,4\n0,4\n6,4\n1,1\n6,1\n1,0\n0,5\n1,6\n2,0")))

(time (try (-main [71 71] 1024 INPUT) (catch Throwable e e)))
; => 336