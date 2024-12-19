(ns aoc.day-4.part-1
  (:require [aoc.day-4.common :refer [diagonally horizontally vertically]]
            [aoc.utils :refer [slurp-input]]
            [clojure.string :as str]))

(defonce INPUT (slurp-input))

(defn -main
  []
  (-> {:word          "XMAS"
       :reversed-word "SAMX"
       :input         (str/split-lines INPUT)
       :acc           0}
      horizontally
      vertically
      diagonally
      :acc))

(-main)
; => 2517
