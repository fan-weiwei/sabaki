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

(defn bitString-to-hexString [string]
  (->> string
    (map #(Character/digit % 2))
    (partition 4)
    (map #(take-n-bits-to-byte 4 %))
  )
)

(defn slurp-ascii-to-bytes [file]
  (->>
    (slurp file)
    (map int)
  )
)

(defn xor-bytes-with-char [char bytes]
  (map #(bit-xor (int char) %) bytes)
)

(defn encode-repeating-key []
  (->>
    (slurp-ascii-to-bytes "repeating-key-test")
    (xor-bytes-with-char \I)
    (map byte-to-bits)
    (apply str)
    (map #(Character/digit % 2))
    ;(partition 4)
    ;(map reverse)
    ;(map #(map bit-shift-left % (range)))
    ;(map #(reduce + %))
    ;(map #(get hex-chars %))
    ;(apply str)
  )
)

(defn -main [& args]
  (->> (encode-repeating-key)
       (map #(Character/digit % 2))
       (partition 4)
       (map
         #(reduce + (for [x (range 4 0 -1)]
                      (bit-shift-left (nth % x) (- 3 x)))
                  ))


       )

  (def poetry
    (->>
      (slurp "repeating-key-test")
      (map int)
      (map #(bit-xor (int \I) %))
      (map byte-to-bits)
      (apply str)
      (map #(Character/digit % 2))
      (partition 4)
      (reduce +)
      )
  )


  (println poetry)

)
