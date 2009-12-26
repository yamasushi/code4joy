;
; dynamical system
;
(ns sgm)

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

;
; main
;
(ns chaos-main (:require picture))

(def  max-iter 2000)
(def  num-traj 100)
(def  rand-seq (repeatedly #(rand) ))
(def  pt0-seq (map (fn [t] [(Math/cos (* 2.0 Math/PI t)) (Math/sin (* 2.0 Math/PI t))]) rand-seq))

(def  calc-next sgm/sgm-map)
(defn ds[pt0]  (iterate calc-next pt0))

(def ds-seq (for [pt0 (take num-traj pt0-seq) p (take max-iter (ds pt0))] p ))

(def image-size [1000 1000])
(def data-frame (reduce 
              (fn [f p] 
                (let[ [minx miny maxx maxy] f
                      [x y]                 p ]
                  [(min minx x) (min miny y) (max maxx x) (max maxy y)]
                ) ) [0 0 0 0] ds-seq))

(defn aspect-ratio
  ([width height] (/ (float width) (float height)) )
  ([minx miny maxx maxy] (aspect-ratio (- maxx minx) (- maxy miny) ) )
)

(defn transform [pt]
  (let [[x y] pt]
  [(* x 100),(* y 100)]
  ))

(picture/create-png image-size "a.png" (fn [g]
  (doseq [p ds-seq]
    ;(println p)
    (picture/point g (transform p)) 
  )
))
