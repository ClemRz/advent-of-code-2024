(ns aoc.day-12.common
  (:require [clojure.string :as str]))

(defn- scan-row
  [r row]
  (map-indexed (fn [c p] {:chr p :pos [r c]}) row))

(defn scan
  [input]
  (let [matrix (mapcat scan-row (range) (str/split-lines input))]
    {:index (reduce (fn [acc {:keys [chr pos]}] (update acc chr conj pos)) {} matrix)}))

#_(defn- cartesian
    [[r-max c-max]]
    (for [r (range r-max)
          c (range c-max)]
      [r c]))

(defn- contiguous?
  [[ra ca] [rb cb]]
  (or (and (= ra rb) (= 1 (abs (- cb ca))))
      (and (= ca cb) (= 1 (abs (- rb ra))))))

(defn- group-contiguous?
  [group pos]
  (some (partial contiguous? pos) group))

(defn vec-remove
  "remove elem in coll"
  [pos coll]
  (into (subvec coll 0 pos) (subvec coll (inc pos))))

(defn regroup
  ([[first-position & positions]]
   (regroup 0 (vec positions) 0 [[first-position]] false))
  ([i positions j groups found?]
   (let [i-max (count positions)]
     (cond
       (empty? positions) groups
       (and (>= i i-max) found?) (recur 0 positions j groups false)
       (and (>= i i-max) (not found?)) (recur 0 (vec (rest positions)) (inc j) (conj groups [(first positions)]) false)
       (group-contiguous? (nth groups j) (nth positions i)) (recur i (vec-remove i positions) j (update groups j conj (nth positions i)) true)
       :else (recur (inc i) positions j groups found?)))))

(defn build-regions
  [{:keys [index] :as m}]
  (reduce
    (fn [acc chr]
      (update acc :grouped assoc chr (regroup (get index chr))))
    m (keys index)))

(defn measure-areas
  [{:keys [grouped index] :as m}]
  (reduce
    (fn [acc chr]
      (update acc :areas assoc chr (pmap count (get grouped chr))))
    m (keys index)))

(defn- decrease
  [result i j]
  (let [f (comp (partial max 0) dec)]
    (-> (update result i f)
        (update j f))))

(defn- perimeter
  ([group]
   (let [c (count group)]
     (reduce + (perimeter (dec c) 0 (vec group) (vec (repeat c 4)) 1))))
  ([i-max i group result j]
   (cond
     (> i i-max) result
     (> j i-max) (recur i-max (inc i) group result (+ i 2))
     (contiguous? (nth group i) (nth group j)) (recur i-max i group (decrease result i j) (inc j))
     :else (recur i-max i group result (inc j)))))

(defn measure-perimeters
  [{:keys [grouped index] :as m}]
  (reduce
    (fn [acc chr]
      (update acc :perimeters assoc chr (map perimeter (get grouped chr))))
    m (keys index)))

(defn compute-cost
  [{:keys [index areas perimeters]}]
  (reduce
    (fn [acc chr]
      (+ acc (reduce + (map * (get areas chr) (get perimeters chr)))))
    0 (keys index)))