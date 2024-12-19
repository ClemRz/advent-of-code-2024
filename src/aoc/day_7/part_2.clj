(ns aoc.day-7.part-2
  (:require [aoc.day-7.common :refer [compute scan]]
            [aoc.utils :refer [slurp-input]]
            [clojure.string :as str]))

(defonce INPUT (slurp-input))

(defn- concatenate
  [a b]
  (parse-long (str a b)))

(def OPERATORS [+ * concatenate])

(defn -main
  []
  (->> INPUT #_"190: 10 19\n3267: 81 40 27\n83: 17 5\n156: 15 6\n7290: 6 8 6 15\n161011: 16 10 13\n192: 17 8 14\n21037: 9 7 18 13\n292: 11 6 16 20"
       str/split-lines
       (map scan)
       (compute OPERATORS)))

(-main)
; => 340362529351427
