(ns aoc.day-8.common
  (:require [clojure.math.combinatorics :as combo]
            [clojure.string :as str]))

(defn- in-the-map?
  [{:keys [max]} [r c]]
  (let [[max-r max-c] max]
    (and (<= 0 r max-r)
         (<= 0 c max-c))))

(defn anti-nodes-v1
  [& positions]
  (let [[[ra ca] [rb cb]] (map :pos positions)
        dr (- rb ra)
        dc (- cb ca)]
    [[[(- ra dr) (- ca dc)]] [[(+ rb dr) (+ cb dc)]]]))

(defn anti-nodes-v2
  [& positions]
  (let [[[ra ca] [rb cb]] (map :pos positions)
        dr (- rb ra)
        dc (- cb ca)
        an1 (map #(vector (- ra (* dr %)) (- ca (* dc %))) (range))
        an2 (map #(vector (+ rb (* dr %)) (+ cb (* dc %))) (range))]
    [an1 an2]))

(defn compute
  [anti-nodes-fn {:keys [index] :as m}]
  (reduce
    (fn [acc [_ positions]]
      (reduce
        (fn [acc [a b]]
          (reduce
            (fn [acc anti-nodes]
              (reduce
                (fn [acc anti-node]
                  (conj acc anti-node))
                acc (take-while (partial in-the-map? m) anti-nodes)))
            acc (anti-nodes-fn a b)))
        acc (combo/combinations positions 2)))
    #{} index))

(defn- scan-row
  [r row]
  (map-indexed (fn [i c] {:chr c :pos [r i]}) row))

(defn scan
  [input]
  (let [[first-line :as lines] (str/split-lines input)
        matrix (->> lines
                    (map-indexed scan-row)
                    (mapcat identity))
        index (dissoc (group-by :chr matrix) \.)]
    {:matrix    matrix
     :directory (reduce (fn [acc {:keys [chr pos]}] (assoc acc pos chr)) {} matrix)
     :index     index
     :max       (map (comp dec count) [lines first-line])}))
