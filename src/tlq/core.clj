(ns tlq.core)

;; initialize our global vector ref
(def my-list (ref (vec nil)))

(defn thread-test [n]
  ;; spawn n threads to add items to the list and then
  ;; loop until they have all finished. 
 (do
   (dotimes [i n]
     ;; spawn our threads.  we create an an anonymous function
     ;; taking advantage of closures to add 'i' to the list and
     ;; update our counter, all within a transaction.
     (.start (Thread. (fn []
                        (dosync (ref-set my-list
                                         (conj @my-list i)))))))
   (loop [my-count (count @my-list)]
     ;; loop until our barrier has reached its limit, then
     ;; print out the vector.
     (if (< my-count n)
       (recur (count @my-list))
       @my-list))))

(defn usage []
  (do
    (println "usage: lein run <n>")
    (println "  n = number of threads")))

(defn -main [& args]
  (if (= (count args) 1)
    (println (thread-test (read-string (first args))))
    (usage)))
