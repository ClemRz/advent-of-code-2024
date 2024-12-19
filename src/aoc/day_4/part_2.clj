(ns aoc.day-4.part-2
  (:require [aoc.day-4.common :refer [diagonally]]
            [aoc.utils :refer [slurp-input]]
            [clojure.string :as str]))

(defonce INPUT (slurp-input))

(defonce M {:acc           0
            :word          "MAS"
            :reversed-word "SAM"})

(defn- thirds
  [s]
  (map-indexed (fn [i _] (apply str (take 3 (drop i s)))) s))

(defn- sub-matrix-3x1
  "Returns the 3x1 sub-matrix starting at row r and column c"
  [r c matrix]
  (let [rows (take 3 (drop r matrix))]
    (map (fn [row] (nth row c)) rows)))

(defn -main
  []
  (let [sub-ms (->> (str/split-lines INPUT)
                    (map thirds))]
    (->> (for [r (range (count sub-ms))
               c (range (- (count (first sub-ms)) 2))]
           (->> sub-ms
                (sub-matrix-3x1 r c)
                (assoc M :input)
                diagonally
                :acc
                (= 2)))
         (filter true?)
         count)))

(-main)
; => 1960
