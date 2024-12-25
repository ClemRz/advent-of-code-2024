(ns aoc.day-17.part-1
  (:require [aoc.day-17.common :refer [run-instructions scan]]
            [aoc.utils :refer [slurp-input]]
            [clojure.string :as str]
            [clojure.test :refer [is]]))

(defonce INPUT (slurp-input))

(defn -main
  [input]
  (->> input
       scan
       run-instructions
       :output
       (str/join ",")))

(is (= 1 (:B (run-instructions {:A 0 :B 0 :C 9 :program [2 6] :pointer 0 :output [] :pct 0}))))
(is (= [0 1 2] (:output (run-instructions {:A 10 :B 0 :C 0 :program [5 0 5 1 5 4] :pointer 0 :output [] :pct 4}))))
(is (= {:output [4 2 5 6 7 7 7 7 3 1 0] :A 0} (select-keys (run-instructions {:A 2024 :B 0 :C 0 :program [0 1 5 4 3 0] :pointer 0 :output [] :pct 4}) [:output :A])))
(is (= 26 (:B (run-instructions {:A 0 :B 29 :C 0 :program [1 7] :pointer 0 :output [] :pct 0}))))
(is (= 44354 (:B (run-instructions {:A 0 :B 2024 :C 43690 :program [4 0] :pointer 0 :output [] :pct 0}))))

(is (= "4,6,3,5,6,3,5,2,1,0" (-main "Register A: 729\nRegister B: 0\nRegister C: 0\n\nProgram: 0,1,5,4,3,0")))

(time (try (-main INPUT) (catch Throwable e e)))
; => 6,4,6,0,4,5,7,2,7