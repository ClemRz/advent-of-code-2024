(ns aoc.day-13.common
  (:require [clojure.string :as str]))

(defn- scan-row
  [row]
  (->> row
       (re-matches #".+\: X.(\d+), Y.(\d+)")
       rest
       (map parse-long)
       (zipmap [:x :y])))

(defn- scan-machine
  [acc [button-a button-b prize]]
  (conj acc {:a     (scan-row button-a)
             :b     (scan-row button-b)
             :prize (scan-row prize)}))

(defn scan
  [input]
  (reduce scan-machine (list) (partition-all 4 (str/split-lines input))))

(defn- compute-tb
  [{xa :x ya :y} {xb :x yb :y} {xp :x yp :y}]
  (/ (- (* xa yp) (* xp ya))
     (- (* xa yb) (* xb ya))))

(defn- compute-ta
  [b {xa :x} {xb :x} {xp :x}]
  (/ (- xp (* xb b))
     xa))

(defn- compute-machine
  [{:keys [a b prize] :as machine}]
  (let [tb (compute-tb a b prize)]
    (-> (update machine :a assoc :pushes (compute-ta tb a b prize))
        (update :b assoc :pushes tb))))

(defn compute-machines
  [machines]
  (map compute-machine machines))

(defn- cost
  [ignore-limit? ppp {:keys [pushes]}]
  (if (and (int? pushes) (or ignore-limit? (<= pushes 100)))
    (* pushes ppp)
    -1))

(defn- compute-cost
  [ignore-limit? a-ppp b-ppp {:keys [a b] :as machine}]
  (let [costs (map (partial cost ignore-limit?) [a-ppp b-ppp] [a b])]
    (assoc machine
      :cost (if (some neg? costs)
              0
              (reduce + costs)))))

(defn compute-costs
  [ignore-limit? a-ppp b-ppp machines]
  (map (partial compute-cost ignore-limit? a-ppp b-ppp) machines))

(defn- adjust-machine
  [adj machine]
  (-> (update-in machine [:prize :x] + adj)
      (update-in [:prize :y] + adj)))

(defn adjust-machines
  [adj machines]
  (map (partial adjust-machine adj) machines))
