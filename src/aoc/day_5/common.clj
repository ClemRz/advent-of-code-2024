(ns aoc.day-5.common
  (:require [clojure.string :as str]))

(defn- index-of
  [v x]
  (some identity (map-indexed (fn [i xx] (when (= xx x) i)) v)))

(defn- correct-order?
  "page x must be printed at some point before page y"
  [[x y] pages index page-number]
  (let [check? (= page-number x)
        y-index (and check? (index-of (take index pages) y))]
    {:x           x
     :y           y
     :x-index     index
     :y-index     y-index
     :pages       pages
     :page-number page-number
     :failed      (and check? y-index)}))

(defn- invalid?
  "if an update includes both page x and page y, then page x must be printed at some point before page y"
  [pair pages]
  (let [pn-set (set pages)]
    (and (every? true? (map (partial contains? pn-set) pair))
         (some #(when (:failed %) %) (map-indexed (partial correct-order? pair pages) pages)))))

(defn- scan-rule
  "HOF"
  [s]
  (partial invalid? (map parse-long (str/split s #"\|"))))

(defn- scan-pages
  [s]
  (map parse-long (str/split s #",")))

(defn scan-document
  [input]
  (let [[rules [_ & updates]] (->> (str/split-lines input)
                                   (split-with (partial not= "")))]
    {:rules   (map scan-rule rules)
     :updates (map scan-pages updates)}))

(defn correctly-ordered?
  [rules pages]
  (every? not ((apply juxt rules) pages)))

(defn middle
  [pages]
  (let [c (count pages)]
    (when (even? c)
      (throw (ex-info "Can't return the middle of an even number of pages" pages)))
    (nth pages (/ c 2))))
