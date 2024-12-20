(ns aoc.day-15.common
  (:require [aoc.utils :refer [char-vector->char-matrix]]
            [clojure.string :as str]))

(defonce LEFT \<)
(defonce RIGHT \>)
(defonce UP \^)
(defonce DOWN \v)

(defonce BOX \O)
(defonce L-BOX \[)
(defonce R-BOX \])
(defonce ROBOT \@)
(defonce WALL \#)
(defonce EMPTY \.)

(defonce SCALING-RULES {WALL  [WALL WALL]
                        BOX   [L-BOX R-BOX]
                        EMPTY [EMPTY EMPTY]
                        ROBOT [ROBOT EMPTY]})

(defn- scan-row
  [r row]
  (map-indexed (fn [c chr] {:chr chr :pos [r c]}) row))

(defn- scale
  [warehouse]
  (map (comp flatten (partial replace SCALING-RULES)) warehouse))

(defn scan
  ([input]
   (scan false input))
  ([scale? input]
   (let [[original-warehouse [_ & moves]] (split-with (comp not str/blank?) (str/split-lines input))
         [first-line :as warehouse] (cond-> original-warehouse scale? scale)
         matrix (mapcat scan-row (range) warehouse)]
     {:directory (reduce (fn [acc {:keys [pos chr]}] (assoc acc pos chr)) {} matrix)
      :catalogue (reduce (fn [acc {:keys [chr pos]}] (update acc chr conj pos)) {} matrix)
      :moves     (str/join moves)
      :size      (map count [warehouse first-line])})))

(defn- displace
  [m obj pos next-pos _movement]
  (cond-> (assoc-in m [:directory next-pos] obj)
          :always (assoc-in [:directory pos] EMPTY)
          :always (update-in [:catalogue obj] (partial remove (partial = pos)))
          :always (update-in [:catalogue obj] conj next-pos)
          (= ROBOT obj) (update :moves subs 1)))

(defn- compute-next-position
  [movement [r c]]
  (condp = movement
    LEFT [r (dec c)]
    RIGHT [r (inc c)]
    UP [(dec r) c]
    DOWN [(inc r) c]))

(defn- step
  [{:keys [directory moves] :as m} obj [r c :as pos]]
  (let [movement (first moves)
        next-pos (compute-next-position movement pos)
        next-obj (get directory next-pos)]
    (if (and (contains? #{L-BOX R-BOX} obj) (contains? #{UP DOWN} movement))
      (let [other-pos [r (if (= L-BOX obj) (inc c) (dec c))]
            other-obj (get directory other-pos)
            other-next-pos (compute-next-position movement other-pos)
            other-next-obj (get directory other-next-pos)
            next-objs [next-obj other-next-obj]]
        (cond
          ;?# or #?
          (some #{WALL} next-objs) (update m :moves subs 1)

          ;..
          (every? #{EMPTY} next-objs) (-> (displace m other-obj other-pos other-next-pos movement)
                                          (displace obj pos next-pos movement))

          ;[]
          (= next-obj obj) (recur m next-obj next-pos)

          ;][
          (every? #{L-BOX R-BOX} next-objs) (let [next-m (step m next-obj next-pos)]
                                              (if (apply = (map :directory [m next-m]))
                                                ; Couldn't move
                                                (update m :moves subs 1)
                                                ; One could move, let's see the other
                                                (let [other-next-m (step next-m other-next-obj other-next-pos)]
                                                  (if (apply = (map :directory [next-m other-next-m]))
                                                    ; Couldn't move
                                                    (update m :moves subs 1)
                                                    ; Could move, let's go
                                                    (recur other-next-m obj pos)))))

          ;.[
          (= EMPTY next-obj) (recur m other-next-obj other-next-pos)

          ;].
          (= EMPTY other-next-obj) (recur m next-obj next-pos)
          :else (do
                  (println {:obj obj :pos pos :movement movement :next-obj next-obj :next-pos next-pos :other-next-obj other-next-obj :other-next-pos other-next-pos})
                  (throw (ex-info "Ran into the void" m)))))
      (condp = next-obj
        WALL (update m :moves subs 1)
        EMPTY (displace m obj pos next-pos movement)
        BOX (recur m next-obj next-pos)
        L-BOX (recur m next-obj next-pos)
        R-BOX (recur m next-obj next-pos)
        (do
          (println {:obj obj :pos pos :movement movement :next-obj next-obj :next-pos next-pos})
          (throw (ex-info "Ran into the void" m)))))))

(defn print-warehouse
  [{:keys [size directory]}]
  (let [[height width] size]
    (->> (for [r (range height)
               c (range width)]
           (get directory [r c] " "))
         (char-vector->char-matrix width)
         println)))

(defn move
  [{:keys [moves] :as m}]
  #_(print-warehouse m)
  (if (str/blank? moves)
    m
    (->> (get-in m [:catalogue ROBOT])
         first
         (step m ROBOT)
         recur)))

(defn gps
  ([m]
   (gps BOX m))
  ([box {:keys [catalogue]}]
   (->> (get catalogue box)
        (map (fn [[r c]] (+ (* 100 r) c))))))