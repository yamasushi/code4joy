(load-file "picture_file.clj")

(def  a    -0.9)
(def  b    0.96)
(def  c   -4.0 )
(def  d    0.9 )
(defn pi  [t] (* t t))
(defn psi [t] (* t t))
(def  max-iter 2000)
(def  num-traj 100)
(def  rand-seq (repeatedly #(rand) ))
(def  pt0-seq (map (fn [t] [(Math/cos (* 2.0 Math/PI t)) (Math/sin (* 2.0 Math/PI t))]) rand-seq))

(defn sgm-map [pt]
  (let [ [x y] pt]
    ;(println x y)
    [ (+ (* a x) (* b y) (/ (+ (* c (pi x) ) d ) (+ 1 (psi x) ) )) , (- x)]
    ))


(def  calc-next sgm-map)
(defn ds[pt0]  (iterate calc-next pt0))

(def ds-seq (for [pt0 (take num-traj pt0-seq) p (take max-iter (ds pt0))] p ))

(defn transform [pt]
  (let [[x y] pt]
  [(* x 100),(* y 100)]
  ))

(create-picture-png 1000 600 "a.png" (fn [g]
  (doseq [p ds-seq]
    ;(println p)
    (draw-point g (transform p)) 
  )
))

;(create-picture-png 1000 600 "a.png" (fn [g]
;  (loop [pt [-1.0 , 0.0] , n 100000]
;    ;(println (transform pt))
;    (let [[x y] (transform pt)] (.drawLine g x y x y) )
;    (if (zero? n)
;      pt
;      (recur (calc-next pt) (dec n) )))
;      
;      ))

