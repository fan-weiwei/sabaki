(ns sabaki.core
  (require [clojure.core.async
            :as a
            :refer [>! <! >!! <!! go chan buffer close! thread
                    alts! alts!! timeout]])
  (require [sabaki.encoding :refer :all])
  (require [sabaki.ops :refer :all]))


(defn sleaze-single-cipher [file]

   ; Get lines
   (def lines (doall
               (->> (slurp file)
                    (clojure.string/split-lines))))

    ; Define break for single line
   (defn process [cipher-text channel]
      (go (let [result (sleaze-single-xor cipher-text)]
          (if (not-empty result) (>! channel result))))
   )

    ; Make channels for each line
   (let [ chans (mapv #(vector % (chan)) lines)
          chans-only (mapv second chans) ]

      (doseq [pair chans] (apply process pair))

      (let [[v p] (alts!! chans-only)] (println "Result: " v))
   )
)

(defn -main [& args]
  (time
    (sleaze-single-cipher "single-cipher")
  )
)
