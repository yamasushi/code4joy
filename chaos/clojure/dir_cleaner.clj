; process dir
;
(import '(java.io File))

(defn main []
  (let [ master_dir       (File. "c:/Users/shuji/Pictures/Gumowski-Mira" )
         files            (-> master_dir file-seq) 
         target_files     (filter #(re-find #"^[^().]+$" (.getName %)) files) ]
      (doseq [f target_files]
        ;; delete ini files
        (let [ fseq          (-> f file-seq)
               inifiles_seq  (filter #(re-find #"\.ini$" (.getName %) ) fseq )  ]
          (doseq [ini inifiles_seq]
            (println (str ini) )
            (.delete ini)
          )
          ;; delete empty dir
          (let [count (count fseq)]
            (do
            (println (str f " " count) )
            (if (== count 1) (.delete f) ) )
          )
        )
      )
  )
)


(main)