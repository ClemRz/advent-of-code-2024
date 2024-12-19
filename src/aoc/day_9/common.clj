(ns aoc.day-9.common)

(defn scan
  [input]
  (->> (partition-all 2 input)
       (mapcat
         (fn [i sizes]
           (let [[block-size free-size] (map (comp parse-long str) sizes)]
             [(repeat block-size i)
              (repeat (or free-size 0) nil)]))
         (range))
       (remove empty?)))

(defn checksum
  [disk]
  (->> (interleave disk (range))
       (partition-all 2)
       (reduce
         (fn [acc [n i]]
           (+ acc (* (or n 0) i)))
         0)))
