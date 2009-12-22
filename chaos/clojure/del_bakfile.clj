; delete bak file
;
(import '(java.io File))
(defn main []
  (let [ files            (-> (File. "c:/Users/shuji/.") .listFiles ) 
         bak_files        (filter #(re-find #"\.bak$" (.getName %)) files) ]
      (doseq [f bak_files]
        ( let [ bak_fname (.getName f) ]
          (println (str bak_fname ))
          (.delete f)
        )
      )
  )
)

(main)