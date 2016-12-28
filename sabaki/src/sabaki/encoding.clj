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


(defn byte-to-bits [byte]
  (->>
    (map bit-test (repeat byte) (range 8))
    (reverse)
    (map #(if % 1 0))
    )
  )

(defn base64-to-bits [base64]
                (->>
                  (map bit-test (repeat base64) (range 6))
                  (reverse)
                  (map #(if % 1 0))
                  )
                )


(defn base16-to-bits [base64]
  (->>
    (map bit-test (repeat base64) (range 4))
    (reverse)
    (map #(if % 1 0))
    )
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


(defn nibbles-to-bytes [string]
  (->> string
       (partition 2)
       (mapv #(+ (bit-shift-left (first %) 4) (second %)))
       )
  )

(defn chr-to-nibble [c] (Character/digit c 16))

(defn hexstring-to-bits [string]
  (->> string
       (map chr-to-nibble)
       (nibbles-to-bytes)
       (mapcat byte-to-bits)
   )
)

(defn hexstring-to-bytes [string]
  (->> string
       (map #(.indexOf hex-chars %))
       (mapcat base16-to-bits)
       (partition 8)
       (map reverse)
       (map #(map bit-shift-left % (range)))
       (map #(reduce + %))
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

(defn slurp-ascii-to-bytes [file]
  (->>
    (slurp file)
    (map int)
    )
  )

(defn slurp-base64-to-bytes [file]
  (->>
    (slurp file)
    (filter #(not= \newline %))
    (map char)
    (map #(.indexOf base64-chars %))
    (mapcat base64-to-bits)
    (partition 8)
    (map reverse)
    (map #(map bit-shift-left % (range)))
    (map #(reduce + %))
    )

)

(defn xor-bytes-with-char [char bytes]
  (map #(bit-xor (int char) %) bytes)
)

