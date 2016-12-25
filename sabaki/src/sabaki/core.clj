(ns sabaki.core
  (require [clojure.core.async
            :as a
            :refer [>! <! >!! <!! go chan buffer close! thread
                    alts! alts!! timeout]])
  (require [sabaki.encoding :refer :all])
  (require [sabaki.ops :refer :all]))

(defn slurp-repeating-key []
  (->>
    (slurp "repeating-key")
    (filter #(not= \newline %))
    (map char)
    (map #(.indexOf base64-chars %))
    (last)
    (get base64-chars)
    (str)
    )
)


(defn -main [& args]
  (println (encode-repeating-key "repeating-key-test" "ICE"))
)
