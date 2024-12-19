(ns aoc.day-11.common
  (:require [clojure.string :as str]))

(defn mutate-one
  [stone]
  (if (zero? stone)
    1
    (let [s (str stone)
          c (count s)]
      (if (even? c)
        (map (comp parse-long (partial apply str)) (split-at (/ c 2) s))
        (* stone 2024)))))

(def m-mutate-one (memoize mutate-one))

(defn mutate
  [stones]
  (reduce
    (fn [acc stone]
      (let [stone (m-mutate-one stone)]
        (cond
          (sequential? stone) (reduce conj acc stone)
          :else (conj acc stone))))
    [] stones))

(defn blink
  [times stones]
  (cond
    (zero? times) stones
    :else (->> (mutate stones)
               (blink (dec times)))))

(defn scan
  [input]
  (->> (str/split input #" ")
       (map parse-long)))
