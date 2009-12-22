; process filename
;
(import '(java.io File))

(defn main []
  (let [ master_dir       (File. "c:/Users/shuji/Pictures/Gumowski-Mira" )
         home_dir         (File. "c:/Users/shuji/Pictures/Gumowski-Mira" )
         files            (-> master_dir file-seq) 
         target_files     (filter #(re-find #"gmchaos_[^()]+.+\.png$" (.getName %)) files) ]
      (doseq [f target_files]
        (let [new_dir_name  (first (re-seq #"gmchaos_[^()]+" (.getName f) ) )
              new_dir       (File. home_dir new_dir_name) 
              new_f         (File. new_dir (.getName f) ) ]
          (println (str f "  " new_f  ) )
          (if (not(.exists new_dir)) (.mkdirs new_dir) )
          (.renameTo f new_f )
        )
      )
  )
)



;; WA ---> QW2
;; WING_EB ---> _
;; EB --> _
;; C1 --> CZ1
;; C2 --> CZ0
;; WING_C1 -> CZ1
;; WING_C2 -> CZ0

(main)