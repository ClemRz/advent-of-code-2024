(ns aoc.day-13.part-1
  (:require [aoc.day-13.common :refer [compute-machines compute-costs scan]]
            [aoc.utils :refer [slurp-input]]
            [clojure.test :refer [is]]))

(defonce INPUT (slurp-input))
(defonce A-PPP 3)
(defonce B-PPP 1)

(defn -main
  [input]
  (->> input
       scan
       compute-machines
       (compute-costs false A-PPP B-PPP)
       (transduce (map :cost) +)))

(is (= 480 (-main "Button A: X+94, Y+34\nButton B: X+22, Y+67\nPrize: X=8400, Y=5400\n\nButton A: X+26, Y+66\nButton B: X+67, Y+21\nPrize: X=12748, Y=12176\n\nButton A: X+17, Y+86\nButton B: X+84, Y+37\nPrize: X=7870, Y=6450\n\nButton A: X+69, Y+23\nButton B: X+27, Y+71\nPrize: X=18641, Y=10279")))

(time (try (-main INPUT) (catch Throwable e e)))
; => 35997