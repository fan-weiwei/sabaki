(ns sabaki.galois)

(defn gx02 "multiplication by 2 in a galois field" [x]

  (cond-> (bit-and 0xff (bit-shift-left x 1))
          (bit-test x 7) (bit-xor 0x1b))

  )

(defn gx03 "multiplication by 3 in a galois field" [x]
  (bit-xor (gx02 x) x)
  )

(defn gx09 "multiplication by 9 in a galois field" [x]
  (bit-xor (gx02 (gx02 (gx02 x))) x)
  )

(defn gx0B "multiplication by 11 in a galois field" [x]
  (let [nine (gx09 x)
        two  (gx02 x)]
    (bit-xor nine two))
  )

(defn gx0D "multiplication by 13 in a galois field" [x]
  (let [nine (gx09 x)
        four (gx02 (gx02 x))]
    (bit-xor nine four))
  )

(defn gx0E "multiplication by 14 in a galois field" [x]
  (let [eight (gx02 (gx02 (gx02 x)))
        four  (gx02 (gx02 x))]
    (bit-xor (bit-xor eight four) (gx02 x)))
  )