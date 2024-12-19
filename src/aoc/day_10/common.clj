(ns aoc.day-10.common
  (:require [clojure.string :as str]))

(defn- next-steps
  [{:keys [elev pos]} directory]
  (let [[r c] pos]
    (->> [[(dec r) c]
          [r (inc c)]
          [(inc r) c]
          [r (dec c)]]
         (map (partial get directory))
         (filter (comp (partial = (inc elev)) :elev)))))

(defn- hike
  [directory {:keys [elev] :as current-step}]
  (let [next-steps (next-steps current-step directory)]
    (cond
      ; Dead end
      (empty? next-steps) nil
      ; Goal reached
      (= 8 elev) next-steps
      ; Keep hiking each option
      :else (flatten (pmap (partial hike directory) next-steps)))))

(defn rate
  [directory trailhead]
  (->> trailhead
       (hike directory)
       (remove nil?)))

(defn score
  [directory trailhead]
  (->> trailhead
       (rate directory)
       distinct
       count))

(defn rate-all
  [{:keys [trailheads directory]}]
  (pmap (comp count (partial rate directory)) trailheads))

(defn score-all
  [{:keys [trailheads directory]}]
  (pmap (partial score directory) trailheads))

(defn- scan-row
  [r row]
  (map-indexed (fn [i e] {:elev (parse-long (str e)) :pos [r i]}) row))

(defn scan
  [input]
  (let [[first-line :as lines] (str/split-lines input)
        matrix (->> lines
                    (map-indexed scan-row)
                    (mapcat identity))]
    {:matrix     matrix
     :directory  (reduce (fn [acc {:keys [pos] :as x}] (assoc acc pos x)) {} matrix)
     :max        (map (comp dec count) [lines first-line])
     :trailheads (get (group-by :elev matrix) 0)}))
