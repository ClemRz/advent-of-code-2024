(ns aoc.day-18.common
  (:require [clojure.string :as str]))

(defonce EMPTY \.)
(defonce CORRUPTED \#)
(defonce STEP-COST 1)

(defn- scan-row
  [row]
  (reverse (map parse-long (str/split row #","))))

(defn scan
  [[y-max x-max :as size] input]
  (let [corr (map scan-row (str/split-lines input))
        corrupted (set corr)
        matrix (for [y (range y-max) x (range x-max)]
                 (let [pos [y x]] {:chr (if (corrupted pos) CORRUPTED EMPTY) :pos pos}))]
    {:corrupted     corr
     :directory     (reduce (fn [acc {:keys [pos chr]}] (assoc acc pos chr)) {} matrix)
     :size          size
     :exit-position (map dec size)
     :entry         {:position [0 0]}}))

(defn init
  [b {:keys [corrupted] :as m}]
  (update m :directory #(reduce
                          (fn [acc n]
                            (assoc acc (nth corrupted n) EMPTY))
                          % (reverse (range b (count corrupted))))))

(defn- viable?
  [{:keys [directory exit-position]} next-pos]
  (or (= exit-position next-pos)
      (= EMPTY (get directory next-pos))))

(defn cost
  [steps]
  (* (or steps 0) STEP-COST))

(defn- neighbors
  [m {:keys [position]}]
  (let [[r c] position]
    (reduce
      (fn [acc next-position]
        (if (viable? m next-position)
          (assoc acc {:position next-position} (cost 1))
          acc))
      {} [[r (dec c)] [r (inc c)] [(dec r) c] [(inc r) c]])))

(defn- min-cost
  [prev-cost new-cost]
  (min (or prev-cost ##Inf) new-cost))

(defn- update-costs
  [m costs visited node]
  (let [current-cost (get costs node)]
    (reduce-kv
      (fn [costs neighbor neighbor-cost]
        (cond-> costs
                (not (visited neighbor)) (update neighbor min-cost (+ current-cost neighbor-cost))))
      costs
      (neighbors m node))))

(defn dijkstra
  [{:keys [exit-position entry] :as m}]
  (loop [costs {entry 0}
         node entry
         visited (hash-set node)]
    (let [c (get costs node ##Inf)]
      (cond
        (or (= (:position node) exit-position) (= ##Inf c)) {:cost c :m m}
        :else (let [next-costs (update-costs m costs visited node)
                    [next-node] (reduce
                                  (fn [[_ prev-cost :as acc] [node cost :as new-acc]]
                                    (if (and (not (visited node)) (= cost (min-cost prev-cost cost)))
                                      new-acc
                                      acc))
                                  [] next-costs)]
                (recur next-costs next-node (conj visited next-node)))))))
