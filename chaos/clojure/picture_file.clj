(import '(java.awt.image BufferedImage))
(import '(java.awt Color))
(import '(java.io File))
(import '(javax.imageio ImageIO))

(defn create-picture-png [width height filename op]
  (let  [ bi (BufferedImage. width height BufferedImage/TYPE_BYTE_GRAY)
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

(create-picture-png 500 600 "a.png" (fn [g] (.drawLine g 0 0 100 100) ) )
