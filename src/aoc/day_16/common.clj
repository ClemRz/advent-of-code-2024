(ns aoc.day-16.common
  (:require [clojure.string :as str]))

(defonce EMPTY \.)
(defonce START \S)
(defonce EXIT \E)
(defonce STARTING-ORIENTATION :E)
(defonce ORIENTATIONS [:N :E :S :W])
(defonce STEP-COST 1)
(defonce TURN-COST 1000)

(defn- scan-row
  [r row]
  (map-indexed (fn [c chr] {:chr chr :pos [r c]}) row))

(defn scan
  [input]
  (let [[first-line :as maze] (str/split-lines input)
        matrix (mapcat scan-row (range) maze)
        catalogue (reduce (fn [acc {:keys [chr pos]}] (update acc chr conj pos)) {} matrix)]
    {:directory     (reduce (fn [acc {:keys [pos chr]}] (assoc acc pos chr)) {} matrix)
     :size          (map count [maze first-line])
     :exit-position (first (get catalogue EXIT))
     :entry         {:position    (first (get catalogue START))
                     :orientation STARTING-ORIENTATION}}))

(defn- compute-orientation
  [pos next-pos]
  (condp = (map - next-pos pos)
    [-1 0] :N
    [0 1] :E
    [1 0] :S
    [0 -1] :W))

(defn- viable?
  [{:keys [directory exit-position]} next-pos]
  (or (= exit-position next-pos)
      (= EMPTY (get directory next-pos))))

(defn cost
  [steps turns]
  (reduce + (map * (map #(or % 0) [steps turns]) [STEP-COST TURN-COST])))

(defn- rotations
  [orientation next-orientation]
  (reduce min
          (map (comp count
                     (partial take-while (comp not #{next-orientation}))
                     (partial drop-while (comp not #{orientation}))
                     cycle)
               [ORIENTATIONS (reverse ORIENTATIONS)])))

(defn- neighbors
  [m {:keys [position orientation]}]
  (let [[r c] position]
    (reduce
      (fn [acc next-position]
        (if (viable? m next-position)
          (let [next-orientation (compute-orientation position next-position)
                turns (rotations orientation next-orientation)]
            (assoc acc {:position    next-position
                        :orientation next-orientation} (cost 1 turns)))
          acc))
      {} [[r (dec c)] [r (inc c)] [(dec r) c] [(inc r) c]])))

(defn- min-cost
  [prev-cost new-cost]
  (min (or prev-cost ##Inf) new-cost))

(defn- update-costs
  [m costs visited position]
  (let [current-cost (get costs position)]
    (reduce-kv
      (fn [costs neighbor neighbor-cost]
        (cond-> costs
                (not (visited neighbor)) (update neighbor min-cost (+ current-cost neighbor-cost))))
      costs
      (neighbors m position))))

(defn dijkstra
  [{:keys [exit-position entry] :as m}]
  (loop [costs {entry 0}
         node entry
         visited (hash-set node)]
    (let [c (get costs node ##Inf)]
      (cond
        (or (= (:position node) exit-position) (= ##Inf c)) c
        :else (let [next-costs (update-costs m costs visited node)
                    [next-node] (reduce
                                  (fn [[_ prev-cost :as acc] [node cost :as new-acc]]
                                    (if (and (not (visited node)) (= cost (min-cost prev-cost cost)))
                                      new-acc
                                      acc))
                                  [] next-costs)]
                (recur next-costs next-node (conj visited next-node)))))))
