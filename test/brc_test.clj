(ns brc-test
  (:require [clojure.test :refer [deftest is testing]]
            [brc]))

(deftest summer-test
  (is (= [3 5 7] (brc/summer [1 2 3] [2 3 4]))))

(deftest words-test
  (is (= 1 (brc/words "hello")))
  (is (= 2 (brc/words "hello world")))
  (is (= 2 (brc/words "Flores,  Petén;35.1")))
  )

(deftest length-test
  (is (= 5 (brc/length "hello")))
  (is (= 7 (brc/length "Zürich")))
  )

(deftest wc-test
  (is (= [ 2 4 20 ] (brc/wc ["hello world", "see you"])))
  )

(deftest echo-test
  (is (= "1 22 3" (brc/echo [1 22 3]))))


(clojure.test/run-tests)
