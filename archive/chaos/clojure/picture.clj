(ns picture
(:import  (java.awt.image BufferedImage)
          (java.awt Color)
          (java.io File)
          (javax.imageio ImageIO) ) )

(defn create-png [size filename op]
  (let  [ [width height] size
          bi (BufferedImage. width height BufferedImage/TYPE_BYTE_GRAY)
          g  (.createGraphics bi) 
          f  (File. filename) ] 
    (.setPaint g Color/WHITE)
    (.fillRect g 0,0,width,height)
    (.setColor g Color/BLACK)
    ;(println g)
    (op g)
    (ImageIO/write bi "png",f)
  )
)


(defn line [g s e] 
  (let [[sx sy] s , [ex ey] e ]
    ;(println sx sy ex ey)
    (.drawLine g sx sy ex ey)
  ))

(defn point [g p] 
  (let [[x y] p]
    (.drawLine g x y x y)
  ))
;