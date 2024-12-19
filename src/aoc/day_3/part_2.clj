(ns aoc.day-3.part-2
  (:require [clojure.test :refer [is]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))

(defn mult
  [args]
  (if (every? some? args)
    (->> args (map parse-long) (apply *))
    0))

(defn lets-go
  [s]
  (let [matcher (re-matcher #"mul\((\d+),(\d+)\)|don't\(\)|do\(\)" s)]
    (loop [enabled? true
           match (re-find matcher)
           acc 0]
      (if-not match
        acc
        (let [[op & args] match
              enabled? (or (and enabled? (not= "don't()" op)) (= "do()" op))]
          (->> (cond-> acc enabled? (+ (mult args)))
               (recur enabled? (re-find matcher))))))))

(is (= 48 (lets-go "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")))

(defn -main
  []
  (lets-go INPUT))

(-main)
; => 112272912
