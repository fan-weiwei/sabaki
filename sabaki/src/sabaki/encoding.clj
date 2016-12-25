(ns sabaki.encoding)

(def hex-chars [\0 \1 \2 \3 \4 \5 \6 \7 \8 \9 \a \b \c \d \e \f])

(def base64-chars [
\A \B \C \D \E \F \G \H \I
 \J \K \L \M \N \O \P \Q \R \S \T \U \V \W
  \X \Y \Z \a \b \c \d \e \f \g \h \i \j
   \k \l \m \n \o \p \q \r \s \t \u \v \w
    \x \y \z \0 \1 \2 \3 \4 \5 \6 \7 \8 \9
     \+ \/ \=
     ])

(defn chr-to-nibble [c] (Character/digit c 16))

(defn nibbles-to-bytes [string]
  (->> string
       (partition 2)
       (mapv #(+ (bit-shift-left (first %) 4) (second %)))
       )
  )

(defn byte-to-bits [byte]
  (->>
    (map bit-test (repeat byte) (range 8))
    (reverse)
    (map #(if % 1 0))
    )
  )

(defn hexstring-to-bits [string]
  (->> string
       (map chr-to-nibble)
       (nibbles-to-bytes)
       (mapcat byte-to-bits)
   )
)

(defn hexstring-to-bytes [string]
  (->> string
       (map chr-to-nibble)
       (nibbles-to-bytes)
       )
  )

(defn take-n-bits-to-byte [n, string]
  (reduce + (for [x (range 0 n)]
              (bit-shift-left (Character/digit (nth string x) 2) (- (- n 1) x))
              ))
)


(defn bits-to-base64String [string]
     (->> string
          (partition 6)
          (map reverse)
          (map #(map bit-shift-left % (range)))
          (map #(reduce + %))
          (map #(get base64-chars %))
          (apply str)
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
       (partition 4)
       (map reverse)
       (map #(map bit-shift-left % (range)))
       (map #(reduce + %)) ; got bytes
       (map #(get hex-chars %))
       (apply str)
   )
 )

(defn bits-to-string [string]
  (->> string
       (bits-to-nibbles)
       (nibbles-to-bytes)
       (bytes-to-string)
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
