(ns aoc.day-10.part-1
  (:require [aoc.day-10.common :refer [scan score-all]]
            [aoc.utils :refer [slurp-input]]
            [clojure.test :refer [is]]))

(defonce INPUT (slurp-input))

(defn -main
  [input]
  (->> input
       scan
       score-all
       (reduce +)))

(is (= 1 (-main "0123\n1234\n8765\n9876")))
(is (= 2 (-main "9990999\n9991999\n9992999\n6543456\n7111117\n8111118\n9111119")))
(is (= 4 (-main "8890779\n8881798\n8882777\n6543456\n7652987\n8762222\n9872222")))
(is (= 3 (-main "1099999\n2757877\n3757777\n4567654\n7758573\n7779552\n7777701")))
(is (= 36 (-main "89010123\n78121874\n87430965\n96549874\n45678903\n32019012\n01329801\n10456732")))

(try (-main INPUT) (catch Throwable e e))
; => 468
