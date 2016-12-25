(ns sabaki.ops
  (require [sabaki.encoding :refer :all]))


(def alphabet (set "etaonsrd "))

(defn xor-with-offset [string n]
  (->> string
       (mapv #(bit-xor n %))
       (bytes-to-string)
  )
)

(defn is-letters? [s]
  (> (/ (count (filter alphabet (clojure.string/lower-case s))) (count s)) 0.47)
)

(defn letter-percentage [string]
  (/
    (->> string
         (clojure.string/lower-case)
         (map #(if (alphabet %) 1 0))
         (reduce +)
    )
  (count string))
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

(defn hex-encode-repeating-key [file key]
  (->>
    (slurp-ascii-to-bytes file)
    (partition 3 3 nil)
    (mapcat #(map bit-xor (map int key) %))
    (mapcat byte-to-bits)
    (bits-to-hexString)
    )
  )

(defn base64-encode-repeating-key [file key]
  (->>
    (slurp-ascii-to-bytes file)
    (partition 3 3 nil)
    (mapcat #(map bit-xor (map int key) %))
    (mapcat byte-to-bits)
    (bits-to-base64String)
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
  (->> bytes
          (partition keysize keysize)
          (partition 2 1)
          (take 20)
          (map #(apply bytes-hamming-distance %))
          (reduce +)
          )
)










