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


