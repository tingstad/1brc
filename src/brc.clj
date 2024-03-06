(ns brc
  (:require [clojure.string :as str]))

(def file "../../dev/1brc/measurements.txt")

(defn readlines [filename num]
  (let [lines (line-seq (clojure.java.io/reader filename))]
    (take num lines)))

(defn words [line]
  (count (str/split line #" +")))

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
  (let [sum (mapv + w1 w2)]
    sum
    ))

(defn summer3 [w1 w2] [(+ (nth w1 0) (nth w2 0))
                      (+ (nth w1 1) (nth w2 1))
                      (+ (nth w1 2) (nth w2 2))])
(def summer2 (partial mapv +))

(defn wc [lines]
  (->> lines
       (map (fn [line] [1 (words line) (+ 1 (length line))]) ,,,)
       (reduce summer  ,,,))
  )

;;(defn main [_opts] (prn (wc (readlines file 10))))

(comment
(prn (wc (readlines file 100000)))
)

(defn echo [result]
  (format "%d %d %d" (get result 0) (get result 1) (get result 2)))

(defn main [_opts]
  (let [lines (line-seq (clojure.java.io/reader *in*))]
    (let [result (wc lines)]
      (println (echo result)))))

(comment
  (wc (readlines file 5))
  )

(summer [1 2 3] [2 5 8])
(map + [1 2 3] [2 5 8])

(summer2 [1 2 3] [2 5 8])
(summer [1 2 3] [2 5 8])
