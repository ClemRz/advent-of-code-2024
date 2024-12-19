(ns aoc.day-9.part-1
  (:require [aoc.day-9.common :refer [checksum scan]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))

(defn compact
  ([disk]
   (compact 0 disk (dec (count disk))))
  ([i disk j]
   (cond
     (>= i j) disk
     (some? (nth disk i)) (recur (inc i) disk j)
     (nil? (nth disk j)) (recur i disk (dec j))
     (nil? (nth disk i)) (recur i (-> (assoc disk i (nth disk j))
                                      (assoc j nil)) j))))

(defn -main
  []
  (-> INPUT #_"2333133121414131402"
      scan
      flatten
      vec
      compact
      checksum))

(try (-main) (catch Throwable e e))
; => 6430446922192
