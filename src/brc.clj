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

(defn summer [w1 w2]
  (mapv + w1 w2))

(defn wc [lines]
  (->> lines
       (map (fn [line] [1 (words line) (+ 1 (length line))]),,,)
       (reduce summer,,,))
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
