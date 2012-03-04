(ns tlq.test.core
  (:use [tlq.core])
  (:use [clojure.test]))

(deftest loss-test
  (do
    (dosync (ref-set tlq.core/my-list (vec nil)))
    (is (= (sort (tlq.core/thread-test 10))
           (range 10)) "Data loss test failed")))
