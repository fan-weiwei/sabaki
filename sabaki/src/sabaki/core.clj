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

(defn get-keysize [data]
  (first
    (->> (range 2 41)
         (map #(block-hamming % data))
         (map float)
         (map vector (range 2 41))
         (sort-by last)
         (first)
         ))
)


(defn -main [& args]
  (let [data (->> (slurp "repeating-key")
                  (map int)
                  (filter #(not= \newline %)))
        keysize (get-keysize data)]

    (println keysize)

    )
)
