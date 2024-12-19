(ns aoc.day-9.part-2
  (:require [aoc.day-9.common :refer [checksum scan]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))

(defn- insert
  [l i i-count j-count]
  (let [[left right] (split-at (inc i) l)]
    (vec (mapcat identity [left (list (repeat (- i-count j-count) nil)) right]))))

(defn- move-it
  [i disk j i-count j-count]
  (cond-> (assoc disk i (nth disk j))
          :always (assoc j (repeat j-count nil))
          (> i-count j-count) (insert i i-count j-count)))

(defn compact
  ([disk]
   (compact 0 disk (dec (count disk))))
  ([i disk j]
   (let [j-count (count (nth disk j))
         i-count (count (filter nil? (nth disk i)))
         i-max (dec (count disk))]
     (cond
       (>= i i-max) disk
       (>= i j) (recur (inc i) disk i-max)
       (some? (first (nth disk i))) (recur (inc i) disk j)
       (nil? (first (nth disk j))) (recur i disk (dec j))
       (>= i-count j-count) (recur i (move-it i disk j i-count j-count) i-max)
       :else (recur i disk (dec j))))))

(defn -main
  []
  (-> INPUT #_"2333133121414131402"
      scan
      vec
      compact
      flatten
      checksum))

(try (-main) (catch Throwable e e))
; => 6460170593016
