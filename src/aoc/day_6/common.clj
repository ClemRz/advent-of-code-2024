(ns aoc.day-6.common
  (:require [clojure.string :as str]))

(defonce OBSTACLE \#)
(defonce INITIAL-POSITION \^)
(defonce CONFIG {:up    {:next-pos-fn    #(update % 0 dec)
                         :next-direction :right}
                 :right {:next-pos-fn    #(update % 1 inc)
                         :next-direction :down}
                 :down  {:next-pos-fn    #(update % 0 inc)
                         :next-direction :left}
                 :left  {:next-pos-fn    #(update % 1 dec)
                         :next-direction :up}})

(defn- out-of-the-map?
  [[r c] {:keys [max]}]
  (let [[max-r max-c] max]
    (not (and (<= 0 r max-r)
              (<= 0 c max-c)))))

(defn- hit-obstacle?
  [pos {:keys [directory]}]
  (->> (get directory (drop-last pos))
       (= OBSTACLE)))

(defn- scan-row
  [r row]
  (map-indexed (fn [i c] {:chr c :pos [r i]}) row))

(defn- on-my-right?
  [[r c] next-direction {:keys [pos]}]
  (let [[obst-r obst-c] pos]
    (case next-direction
      :up (and (= c obst-c) (< obst-r r))
      :right (and (= r obst-r) (< c obst-c))
      :down (and (= c obst-c) (< r obst-r))
      :left (and (= r obst-r) (< obst-c c)))))

(defn- obstacles-on-my-right
  [{:keys [visited index]} pos]
  (let [current-direction (last (last visited))
        next-direction (get-in CONFIG [current-direction :next-direction])]
    (filter (partial on-my-right? pos next-direction) (get index OBSTACLE))))

(declare move)

(defn- looping?
  [m current-pos next-direction]
  (-> (assoc m :compute-loops? false)
      (update :visited conj (assoc current-pos 2 next-direction))
      move
      :looped?))

(defn- visited?
  [{:keys [visited]} next-pos]
  (contains? (set (map drop-last visited)) (drop-last next-pos)))

(defn- update-loops
  [m pos next-pos next-direction]
  (cond-> m
          (and (not (visited? m next-pos))
               (seq (obstacles-on-my-right m pos))
               (looping? m pos next-direction)) (update :loops conj next-pos)))

(defn- back-on-track?
  [next-pos {:keys [visited-set]}]
  (contains? visited-set next-pos))

(defn move
  "Moves until hitting an obstacle, then turns right"
  [{:keys [visited compute-loops?] :as m}]
  (let [[_ _ current-direction :as current-pos] (last visited)
        {:keys [next-pos-fn next-direction]} (get CONFIG current-direction)
        next-pos (next-pos-fn current-pos)]
    (cond
      (out-of-the-map? next-pos m) m
      (back-on-track? next-pos m) (assoc m :looped? true)
      (hit-obstacle? next-pos m) (-> m
                                     (update :visited conj (assoc current-pos 2 next-direction))
                                     (update :visited-set conj (assoc current-pos 2 next-direction))
                                     move)
      :else (cond-> m
                    compute-loops? (update-loops current-pos next-pos next-direction)
                    :always (update :visited conj next-pos)
                    :always (update :visited-set conj next-pos)
                    :always recur))))

(defn- scan
  [input]
  (let [[first-line :as lines] (str/split-lines input)
        matrix (->> lines
                    (map-indexed scan-row)
                    (mapcat identity))
        index (group-by :chr matrix)
        initial-position (-> (get index INITIAL-POSITION) first :pos (conj :up))]
    {:matrix                   matrix
     :directory                (reduce (fn [acc {:keys [chr pos]}] (assoc acc pos chr)) {} matrix)
     :index                    index
     :max                      (map (comp dec count) [lines first-line])
     :visited                  [initial-position]
     :visited-set              #{initial-position}
     :loops                    []
     :compute-loops?           false
     :stop-when-back-on-track? false}))

(defn go
  ([input]
   (go false input))
  ([compute-loops? input]
   (-> input
       scan
       (assoc :compute-loops? compute-loops?)
       move)))
