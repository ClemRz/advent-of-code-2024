(ns aoc.day-4.common
  (:require [clojure.string :as str]))

(defn- count-word
  [s word]
  (count (re-seq (re-pattern word) s)))

(defn horizontally
  [{:keys [input acc word reversed-word] :as m}]
  (->> input
       (reduce
         (fn [acc l]
           (apply + acc (map (partial count-word l) [word reversed-word])))
         acc)
       (assoc m :acc)))

(defn- transpose
  [m]
  (apply mapv vector m))

(defn- transpose-lines
  [lines]
  (->> (transpose lines)
       (map (partial apply str))))

(defn vertically
  [{:keys [input] :as m}]
  (-> m
      (update :input transpose-lines)
      horizontally
      (assoc :input input)))

(defn- straighten-ne-sw
  [lines]
  (let [c (dec (count lines))]
    (->> lines
         (map-indexed (fn [i l] (str/join (concat (repeat i ".") [l] (repeat (- c i) "."))))))))

(defn- straighten-nw-se
  [lines]
  (-> lines
      reverse
      straighten-ne-sw
      reverse))

(defn- ne-sw-ally
  [{:keys [input] :as m}]
  (-> m
      (update :input straighten-ne-sw)
      vertically
      (assoc :input input)))

(defn- nw-se-ally
  [{:keys [input] :as m}]
  (-> m
      (update :input straighten-nw-se)
      vertically
      (assoc :input input)))

(defn diagonally
  [m]
  (-> m
      ne-sw-ally
      nw-se-ally))
