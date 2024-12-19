(ns aoc.day-5.part-2
  (:require [aoc.day-5.common :refer [correctly-ordered? middle scan-document]]
            [aoc.utils :refer [slurp-input]]))

(defonce INPUT (slurp-input))
(defonce MAX-RETRIES 100)

(defn- fix-pages
  [pages rule]
  (let [{:keys [failed y x-index y-index]} (rule pages)]
    (if failed
      (let [[pages1 [_ & pages2]] (split-at y-index pages)
            [pages2 pages3] (split-at x-index pages2)]
        (concat pages1 pages2 [y] pages3))
      pages)))

(defn- fix-update
  ([rules pages]
   (fix-update 0 rules pages))
  ([attempt-nr rules pages]
   (when (>= attempt-nr MAX-RETRIES)
     (throw (ex-info "Too many attempts" {})))
   (if (correctly-ordered? rules pages)
     pages
     (->> (reduce fix-pages pages rules)
          (recur (inc attempt-nr) rules)))))

(defn -main
  []
  (let [{:keys [rules updates]} (scan-document INPUT)]
    (->> (remove (partial correctly-ordered? rules) updates)
         (map (partial fix-update rules))
         (map middle)
         (reduce +))))

(-main)
; => 4480
