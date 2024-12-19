(ns aoc.day-7.common
  (:require [clojure.math.combinatorics :as combo]
            [clojure.string :as str]))

(defn- operate
  [operators [number & numbers]]
  (reduce (fn [{:keys [ops] :as m} number]
            (-> (update m :total (first ops) number)
                (update :ops rest)))
          {:total number :ops operators} numbers))

(defn- true-equation?
  [total numbers operators]
  (= total (:total (operate operators numbers))))

(defn compute
  [operators entries]
  (reduce
    (fn [acc {:keys [total numbers]}]
      (let [combos (combo/selections operators (dec (count numbers)))]
        (cond-> acc
                (some (partial true-equation? total numbers) combos) (+ total))))
    0 entries))

(defn scan
  [line]
  (let [[_ total numbers] (re-matches #"(\d+)\: (.+)" line)]
    {:total   (parse-long total)
     :numbers (map parse-long (str/split numbers #" "))}))
