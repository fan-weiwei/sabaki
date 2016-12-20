(ns sabaki.core
  (require [clojure.core.async
            :as a
            :refer [>! <! >!! <!! go chan buffer close! thread
                    alts! alts!! timeout]])
  (require [sabaki.encoding :refer :all])
  (require [sabaki.ops :refer :all]))

(defn -main [& args]

  (let [begin (System/currentTimeMillis)]

  (defn process [cipher-text channel] (go
           (let [result (sleaze-single-xor cipher-text)]
             (if (> (count result) 0) (>! channel result)))

  ))

  (def lines (doall
               (->> (slurp "single-cipher")
                    (clojure.string/split-lines))))

  (def channels (for [line lines] (chan)))

  (let [ chans (partition-all 2 (interleave lines (for [line lines] (chan))))
         chans-only (mapv second chans) ]

    (doseq [pair chans] (apply process pair))
    (let [[v p] (alts!! chans-only)]
      (println "Result: " v))
      (println "Waited (ms) :" (- (System/currentTimeMillis) begin))
   )
  )
)
