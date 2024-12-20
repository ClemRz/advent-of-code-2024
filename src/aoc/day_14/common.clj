(ns aoc.day-14.common
  (:require [aoc.utils :refer [char-vector->char-matrix]]
            [clojure.string :as str]))

(defn- scan-robot
  [line]
  (->> line
       (re-matches #"p=(\d+),(\d+) v=(-?\d+),(-?\d+)")
       rest
       (map parse-long)
       (partition 2)
       (zipmap [:pos :speed])))

(defn scan
  [input]
  (reduce (fn [acc line] (conj acc (scan-robot line))) [] (str/split-lines input)))

(defn- teleport
  [[size-x size-y :as size] [x y :as pos]]
  (cond
    (neg? x) (recur size [(+ x size-x) y])
    (neg? y) (recur size [x (+ y size-y)])
    (>= x size-x) (recur size [(- x size-x) y])
    (>= y size-y) (recur size [x (- y size-y)])
    :else pos))

(defn- move
  [t s p]
  (+ p (* t s)))

(defn- predict-robot
  [size time {:keys [speed] :as robot}]
  (update robot :pos (comp (partial teleport size) (partial map (partial move time) speed))))

(defn freq
  [robots]
  (frequencies (map :pos robots)))

(defn- print-robots
  [[size-x size-y] time freqs]
  (->> (for [y (range size-y)
             x (range size-x)]
         (get freqs [x y] "."))
       (char-vector->char-matrix size-x)
       println)
  (println time))

(defn- crossed-threshold?
  [size-x time threshold freqs]
  (let [cnt-max (->> (reduce (fn [acc [[x y]]] (update acc y conj x)) {} freqs)
                     (reduce (fn [acc [y xs]] (assoc acc y (sort xs))) {})
                     (reduce (fn [acc [y [x & xs]]]
                               (assoc acc y
                                          (->> (reduce
                                                 (fn [xsacc x]
                                                   (conj xsacc
                                                         {:dst (- x (:x (last xsacc)))
                                                          :x   x}))
                                                 [{:x x}] xs)
                                               (map :dst)
                                               frequencies
                                               vals
                                               (reduce max))))
                             {})
                     vals
                     (reduce max))
        pct (/ cnt-max size-x)]
    (println "Time" time (str (int (* pct 100)) "%"))
    (println (str/join (repeat cnt-max "|")) "\n")
    (-> pct (>= threshold))))

(defn- tree-detected?
  [[size-x :as size] time threshold robots]
  (let [freqs (freq robots)]
    (when (crossed-threshold? size-x time threshold freqs)
      (print-robots size time freqs)
      true)))

(defn predict-robots
  ([size time robots]
   (predict-robots false size time nil robots))
  ([look-4-tree? size time threshold robots]
   (let [robots (map (partial predict-robot size (if look-4-tree? 1 time)) robots)]
     (cond
       look-4-tree? (if (tree-detected? size time threshold robots)
                      time
                      (recur look-4-tree? size (inc time) threshold robots))
       :else robots))))

(defn split
  [[size-x size-y] freqs]
  (reduce
    (fn [acc [[x y] cnt]]
      (update
        acc
        (if (< x (/ (dec size-x) 2))
          (if (< y (/ (dec size-y) 2))
            :nw
            (if (> y (/ size-y 2))
              :ne
              :ditch))
          (if (> x (/ size-x 2))
            (if (< y (/ (dec size-y) 2))
              :sw
              (if (> y (/ size-y 2))
                :se
                :ditch))
            :ditch))
        + cnt))
    {:ne 0 :se 0 :sw 0 :nw 0 :ditch 0} freqs))

(defn safety-factor
  [{:keys [ne se sw nw]}]
  (* ne se sw nw))
