(ns aoc.day-17.part-2
  (:require [aoc.day-17.common :refer [scan search-A]]
            [aoc.utils :refer [slurp-input]]
            [clojure.test :refer [is]]))

(defonce INPUT (slurp-input))

(defn -main
  [input]
  (->> input
       scan
       search-A))

(is (= 117440 (time (-main "Register A: 2024\nRegister B: 0\nRegister C: 0\n\nProgram: 0,3,5,4,3,0"))))

(time (try (-main INPUT) (catch Throwable e e)))
; => 164541160582845

; 2,4 => bst A {store A mod 8 in B} (3 LSB) numbers from 0 to 7: 0 1 2 3 4 5 6 7, repeating pattern
; 1,1 => bxl 1 {store B xor 1 in B} repeating pattern 1 0 3 2 5 4 7 6
; 7,5 => cdv B {store A >> B in C} shifts A to the right B times, stored in C
; 1,5 => bxl 5 {store B xor 5 in B} B XOR 2r101 stored in B
; 4,0 => bxc 0 {store B xor C in B} B XOR C stored in B
; 0,3 => adv 3 {store A >> 3 in A} shifts A to the right 3 times, stored in A
; ^^^^^^^^^^^^ we can step the research by shifting A, 3 bits to the left, and comparing the output with the end of the program
; 5,5 => out B {print B mod 8} outputs B 3 LSB
; 3,0 => jnz 0 {GOTO start} loops until A is zero
