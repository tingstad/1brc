(ns brc
  (:import (java.util HashMap)))

(def file "../../dev/1brc/measurements.txt")

(defn readlines [filename num]
  (let [lines (line-seq (clojure.java.io/reader filename))]
    (take num lines)))

(defn words [line]
  (+ 1 (count
         (filter
           (fn [ch] (= ch \ ))
           line))))

(defn length [line]
  (count (.getBytes line "UTF-8")))

(comment
  (defn summer [w1 w2] [(+ (nth w1 0) (nth w2 0))
                        (+ (nth w1 1) (nth w2 1))
                        (+ (nth w1 2) (nth w2 2))])
  (defn wc [lines]
    (let [countline
          (fn [line]
            [1, (words line), (count line)])]
      (reduce summer
              [0 0 0]
              (map countline lines))))
  )

(defn summer [w1 w2]
  (let [sum (map + w1 w2)]
    sum
    ))

(defn wc [lines]
  (->> lines
       (map (fn [line] [1 (words line) (+ 1 (length line))]),,,)
       (reduce summer [0 0 0],,,))
  )

;;(defn main [_opts] (prn (wc (readlines file 10))))

(defn main [_opts]
  (let [lines (line-seq (clojure.java.io/reader *in*))]
    (prn
      (wc lines)
      )))

(comment
  (wc (readlines file 5))
  )

(summer [1 2 3] [2 5 8])
(map + [1 2 3] [2 5 8])
(def summer2 (partial map +))

(summer2 [1 2 3] [2 5 8])
(summer [1 2 3] [2 5 8])