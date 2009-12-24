(load-file "picture_file.clj")

(defn calc-next [pt]
  (let [ [x y] pt]
    ;(println x y)
    [ (+ 1.0 (- y) (Math/abs x) ) , x]
    ))

(defn transform [pt]
  (let [[x y] pt]
  [(* x 100),(* y 100)]
  ))

(create-picture-png 500 600 "a.png" (fn [g]
  (loop [pt [0.01,0.] , n 1000]
    (println (transform pt))
    (let [[x y] (transform pt)] (.drawLine g x y x y) )
    (if (zero? n)
      pt
      (recur (calc-next pt) (dec n) )))
      
      ))
