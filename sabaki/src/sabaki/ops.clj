(ns sabaki.ops
  (require [sabaki.encoding :refer :all]))


(def alphabet (set "etaoinshrdlu "))

(defn xor-with-offset [string n]
  (->> string
       (hexstring-to-bytes)
       (mapv #(bit-xor n %))
       (bytes-to-string)
  )
)

(defn get-n-gram-freq [n string]
  (->> string
       (.toLowerCase)
       (partition n 1)
       (frequencies)
       )
  )

(defn is-letters? [s]
  (> (/ (count (filter alphabet (clojure.string/lower-case s))) (count s)) 2/3)
)

(defn sleaze-single-xor [cipher-text]

  (->> (range 256)
       (mapv #(xor-with-offset cipher-text %))
       (filter is-letters?)
       (first)
  )

)

(defn encode-repeating-key [file key]
  (->>
    (slurp-ascii-to-bytes file)
    (partition 3 3 nil)
    (mapcat #(map bit-xor (map int key) %))
    (mapcat byte-to-bits)
    (bits-to-hexString)
    )
  )

(defn byte-hamming-weight [byte]
  (->> (map bit-test (repeat byte) (range 8))
       (map #(if % 1 0))
       (reduce +)
       )
)

(defn ascii-hamming-distance [str1 str2]
  (->> (map bit-xor (map int str1) (map int str2))
       (map byte-hamming-weight)
       (reduce +))
)

(defn bytes-hamming-distance [bytes1 bytes2]
  (/ (->> (map bit-xor bytes1 bytes2)
          (map byte-hamming-weight)
          (reduce +)) (count bytes1)))


(defn block-hamming [keysize bytes]
  (/ (->> bytes
          (partition keysize keysize)
          (partition 2 1)
          (take 1)
          (map #(apply bytes-hamming-distance %))
          (reduce +)
          ) 1)
  )









