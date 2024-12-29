(ns aoc.day-18.part-2
  (:require [aoc.day-18.common :refer [EMPTY dijkstra scan]]
            [aoc.utils :refer [slurp-input]]
            [clojure.string :as str]
            [clojure.test :refer [is]]))

(defonce INPUT (slurp-input))

(defn -main
  [size input]
  (loop [m (scan size input)
         b (dec (count (:corrupted m)))]
    (let [{:keys [cost m]} (dijkstra m)]
      (println b (reverse (nth (:corrupted m) b)) cost)
      (cond
        (= ##Inf cost) (recur (assoc-in m [:directory (nth (:corrupted m) b)] EMPTY) (dec b))
        :else (str/join "," (reverse (nth (:corrupted m) (inc b))))))))

(is (= "6,1" (time (-main [7 7] "5,4\n4,2\n4,5\n3,0\n2,1\n6,3\n2,4\n1,5\n0,6\n3,3\n2,6\n5,1\n1,2\n5,5\n2,5\n6,5\n1,4\n0,4\n6,4\n1,1\n6,1\n1,0\n0,5\n1,6\n2,0"))))

(time (try (-main [71 71] INPUT) (catch Throwable e e)))
; => 24,30