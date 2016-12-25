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

(defn get-keysize [limit data]
  (first
    (->> (range 2 (+ limit 1))
         (map #(block-hamming % data))
         (map float)
         (map vector (range 2 41))
         (sort-by last)
         (first)
         ))
)


(defn -main [& args]
  (println
    (let [data (slurp-base64-to-bytes "repeating-key")
          keysize (get-keysize 41 data)
          lines (->> data
                     (partition keysize)
                     (apply map list))]

      (letfn [(process [line]
                (->> (range 256)
                     (mapv #(xor-with-offset line %))
                     (sort-by letter-percentage >)
                     (first)
                     ;(filter is-letters?)
                     ;(first)
                     ))

              ]
        ;(process (second lines))

        (->> (map process lines)
             (apply mapcat list)
             (apply str)

             )

        )))

)
