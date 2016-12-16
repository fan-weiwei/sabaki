(ns sabaki.encoding)

(def ^:private base64-chars
  "Base64 Characters in the right order."
  (vec
    (concat
      (map char
           (concat
             (range (int \A) (inc (int \Z)))
             (range (int \a) (inc (int \z)))
             (range (int \0) (inc (int \9)))))
      [\+ \/ \=])))

(def ^:private hex-chars [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \a \b \c \d \e \f])

(defn chr-to-nibble [c] (Character/digit c 16))

(defn byte-to-bits [b]
  (apply str (reverse
               (for [x (range 0 8)]
                 (if
                   (< 0 (->> x
                             (bit-shift-left 1)
                             (bit-and b)
                             ))
                   1 0
                   ))
               ))
  )

(defn nibbles-to-bytes [string]
  (->> string
       (partition 2)
       (map #(+ (bit-shift-left (nth % 0) 4) (nth % 1)))
       )
  )

(defn hexstring-to-bits [string]
  (->> string
       (map chr-to-nibble)
       (nibbles-to-bytes)
       (map byte-to-bits)
       (apply str)
       )
  )

(defn hexstring-to-bytes [string]
  (->> string
       (map chr-to-nibble)
       (nibbles-to-bytes)
       )
  )

(defn string-to-bytes [string]
  (->> string
       (map chr-to-nibble)
       (map nibbles-to-bytes)
       )
  )

(defn take-n-bits-to-byte [n, string]
  (reduce + (for [x (range 0 n)]
              (bit-shift-left (Character/digit (nth string x) 2) (- (- n 1) x))
              ))
  )

(defn bits-to-base64Bytes [string]
  (->> string
       (partition 6)
       (map #(take-n-bits-to-byte 6 %))
       )
  )

(defn base64Bytes-to-string [bytes]
  (->> bytes
       (map #(get base64-chars %))
       (apply str)
       )
  )

(defn bits-to-base64String [string]
  (->> string
       (bits-to-base64Bytes)
       (base64Bytes-to-string)
       )
  )

(defn pairwise-xor [pair]
  (apply bit-xor pair)
  )

(defn xor-bits [str1 str2]
  (map pairwise-xor
       (map list (map #(Character/digit % 2) str1) (map #(Character/digit % 2) str2)))
  )

(defn bits-to-nibbles [string]
  (->> string
       (partition 4)
       (map #(reduce + (for [x (range 0 4)]
                         (bit-shift-left (nth % x) (- 3 x))
                         )))
       )
  )

(defn nibbles-to-hexstring [bytes]
  (->> bytes
       (map #(get hex-chars %))
       (apply str)
       )
  )

(defn bytes-to-string [bytes]
  (->> bytes
       (map char)
       (apply str)
       )
  )

(defn bits-to-hexString [string]
  (->> string
       (bits-to-nibbles)
       (nibbles-to-hexstring)
       )
  )

(defn bits-to-string [string]
  (->> string
       (bits-to-nibbles)
       (nibbles-to-bytes)
       (bytes-to-string)
       )
  )