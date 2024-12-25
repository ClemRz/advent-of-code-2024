(ns aoc.day-17.common
  (:require [clojure.string :as str]))

(defn- scan-register
  [s]
  (parse-long (first (re-seq #"\d+" s))))

(defn- scan-program
  [s]
  (map parse-long (re-seq #"\d" s)))

(defn scan
  [input]
  (let [[A B C _ P] (str/split-lines input)
        program (scan-program P)]
    {:A       (scan-register A)
     :B       (scan-register B)
     :C       (scan-register C)
     :program program
     :pointer 0
     :output  []
     :pct     (- (count program) 2)}))

(defn- combo
  [{:keys [A B C]} operand]
  (condp = operand
    0 0
    1 1
    2 2
    3 3
    4 A
    5 B
    6 C))

(defn- jump
  [step m]
  (update m :pointer + step))

(def ^:private jump-2 (partial jump 2))

(defn- xdv
  [x {:keys [A] :as m} operand]
  (-> (assoc m x (bit-shift-right A (combo m operand)))
      jump-2))

(def ^:private adv (partial xdv :A))
(def ^:private bdv (partial xdv :B))
(def ^:private cdv (partial xdv :C))

(defn- bxl
  [m operand]
  (-> (update m :B bit-xor operand)
      jump-2))

(defn- bst
  "The bst instruction (opcode 2) calculates the value of its combo operand modulo 8 (thereby keeping only its lowest 3 bits),
  then writes that value to the B register."
  [m operand]
  (-> (assoc m :B (mod (combo m operand) 8))
      jump-2))

(defn- jnz
  [{:keys [A] :as m} operand]
  (if (zero? A)
    (jump-2 m)
    (assoc m :pointer operand)))

(defn- bxc
  [{:keys [B C] :as m} _]
  (-> (assoc m :B (bit-xor B C))
      jump-2))

(defn- out
  [m operand]
  (-> (update m :output conj (mod (combo m operand) 8))
      jump-2))

(def ^:private instructions {0 adv
                             1 bxl
                             2 bst
                             3 jnz
                             4 bxc
                             5 out
                             6 bdv
                             7 cdv})

(defn run-instructions
  [{:keys [pointer program pct] :as m}]
  (if (> pointer pct)
    m
    (let [opcode (nth program pointer)
          operand (nth program (inc pointer))]
      (recur ((get instructions opcode) m operand)))))

(defn search-A
  [{:keys [program] :as m}]
  (loop [levels [[1 0]]]
    (let [[A increments] (first levels)]
      (if (> increments 7)
        (recur (update-in (vec (rest levels)) [0 1] inc))
        (let [a (+ A increments)
              {:keys [output]} (run-instructions (assoc m :A a))]
          (cond
            (= program output) a
            (= (take-last (count output) program) output) (recur (vec (cons [(bit-shift-left a 3) 0] levels)))
            :else (recur (update-in levels [0 1] inc))))))))
