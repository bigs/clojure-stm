(ns tlq.core)

;; initialize our global vector ref
(def my-list (ref (vec nil)))

(defn thread-test [n]
  ;; make a counter ref (barrier) we can use to determine
  ;; when the threads have all terminated.  spawn n threads
  ;; to add items to the list and then loop until they have
  ;; all finished. 
 (let [my-count (ref 0)]
   (dotimes [i n]
     ;; spawn our threads.  we create an an anonymous function
     ;; taking advantage of closures to add 'i' to the list and
     ;; update our counter, all within a transaction.
     (.start (Thread. (fn []
                        (dosync (ref-set my-list
                                         (conj @my-list i))
                          (ref-set my-count (inc @my-count)))))))
   (loop [count @my-count]
     ;; loop until our barrier has reached its limit, then
     ;; print out the vector.
     (if (< count n)
       (recur @my-count)
       (println @my-list)))))

(defn -main [& args]
  (thread-test (read-string (first args))))
