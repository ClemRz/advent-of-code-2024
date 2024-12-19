(ns aoc.day-2.part-2
  (:require [aoc.day-2.common :refer [all-gradual? extract-reports]]
            [aoc.utils :refer [slurp-input]]
            [clojure.test :refer [is]]))

(defonce INPUT (slurp-input))

(defn private-safe?
  [report]
  (and (or (apply > report)
           (apply < report))
       (all-gradual? report)))

(defn vec-remove
  "remove elem in coll"
  [coll pos]
  (try
    (into (subvec coll 0 pos) (subvec coll (inc pos)))
    (catch Exception e
      (println coll pos)
      (println e)
      (throw e))))

(defn sub-reports
  [report]
  (->> (count report)
       range
       (map (partial vec-remove report))
       distinct
       (cons report)))

(defn safe?
  [report]
  (->> (vec report)
       sub-reports
       (some private-safe?)))

(is (safe? [7 6 4 2 1]))
(is (not (safe? [1 2 7 8 9])))
(is (not (safe? [9 7 6 2 1])))
(is (safe? [1 3 2 4 5]))
(is (safe? [8 6 4 4 1]))
(is (safe? [1 3 6 7 9]))

(defn -main
  []
  (->> (extract-reports INPUT)
       (map safe?)
       (filter true?)
       count))

(-main)
; => 710