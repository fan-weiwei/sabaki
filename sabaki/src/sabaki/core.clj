(ns sabaki.core
  (require [clojure.core.async
            :as a
            :refer [>! <! >!! <!! go chan buffer close! thread
                    alts! alts!! timeout]])
  (require [sabaki.encoding :refer :all])
  (require [sabaki.ops :refer :all]))

(defn slurp-repeating-key []
  (def cipher-text
    (->>
      (slurp "repeating-key")
      (filter #(not= \newline %))
      (map char)
      (map #(.indexOf base64-chars %))
      ;(filter #(> 0 (.indexOf base64-chars %)))
      ))

  ;(map #(.indexOf base64-chars %))
  (println cipher-text)
  (println base64-chars)
)


(defn -main [& args]
  (println (encode-repeating-key "repeating-key-test" "ICE"))
)
