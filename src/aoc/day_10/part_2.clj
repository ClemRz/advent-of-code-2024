(ns aoc.day-10.part-2
  (:require [aoc.day-10.common :refer [scan rate-all]]
            [aoc.utils :refer [slurp-input]]
            [clojure.test :refer [is]]))

(defonce INPUT (slurp-input))

(defn -main
  [input]
  (->> input
       scan
       rate-all
       (reduce +)))

(is (= 81 (-main "89010123\n78121874\n87430965\n96549874\n45678903\n32019012\n01329801\n10456732")))

(try (-main INPUT) (catch Throwable e e))
; => 966
