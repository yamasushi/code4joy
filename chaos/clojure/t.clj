(load-file "picture_file.clj")

(def  a    -0.9)
(def  b    0.96)
(def  c   -4.0 )
(def  d    0.9 )
(defn pi  [t] (* t t))
(defn psi [t] (* t t))

(defn sgm-map [pt]
  (let [ [x y] pt]
    ;(println x y)
    [ (+ (* a x) (* b y) (/ (+ (* c (pi x) ) d ) (+ 1 (psi x) ) )) , (- x)]
    ))

(defn transform [pt]
  (let [[x y] pt]
  [(* x 100),(* y 100)]
  ))

(def calc-next sgm-map)
(def ds  (iterate calc-next [-1.0 , 0.0] ))
(def ds2 (rest ds) )

(defn draw-line [g s e] 
  (let [[sx sy] (transform s) , [ex ey] (transform e) ]
    ;(println sx sy ex ey)
    (.drawLine g sx sy ex ey)
  ))

(defn draw-point [g p] 
  (let [[x y] (transform p)]
    (.drawLine g x y x y)
  ))

(create-picture-png 1000 600 "a.png" (fn [g]
  (doseq [p (take 100000 ds)]
    ;(println p)
    (draw-point g p)
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

