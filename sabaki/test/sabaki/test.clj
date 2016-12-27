(ns sabaki.test
  (:require [clojure.test :refer :all]
            [sabaki.core :refer :all]
            [sabaki.encoding :refer :all]
            [sabaki.ops :refer :all])

  (require [clojure.core.async
            :as a
            :refer [>! <! >!! <!! go chan buffer close! thread
                    alts! alts!! timeout]]))

(deftest hex

  (let [hex-string "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
        irl-string "I'm killing your brain like a poisonous mushroom"
        base64-string "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"]


    (testing "Test hex-string to base64"
      (let [result (bits-to-base64String (hexstring-to-bits hex-string))]
        (is (= result base64-string))

        )
      )

  )

)

(deftest base64-slurp
  (testing "Slurp to base64"
    (is (=
          (->>
            (slurp-base64-to-bytes "base64-test")
            (bytes-to-string)
            )
          "I'm killing your brain like a poisonous mushroom"
    )))

)

(deftest keysize-test
  (testing "Slurp to base64"
    (is (=
          (->>
            (base64-encode-repeating-key "repeating-key-test" "ICE")
            (filter #(not= \newline %))
            (map char)
            (map #(.indexOf base64-chars %))
            (mapcat base64-to-bits)
            (partition 8)
            (map reverse)
            (map #(map bit-shift-left % (range)))
            (map #(reduce + %))
            (get-keysize 20))
    )
  )
))

(deftest xor-single-char
  (testing "Test single repeating xor"

    (is (= (->>
             (slurp-ascii-to-bytes "repeating-key-test")
             (xor-bytes-with-char \I)
             (mapcat byte-to-bits)
             (bits-to-hexString)
             )

           "0b3c3b2720272e696e2c246569202f6930263c692820276e3d69383c202a226928272d692720242b252c4300692e26692a3b283330693e212c27690069212c283b6928692a30242b2825"))

    )
)

(deftest xor-repeating-key
  (testing "Test repeating key string"
    (is (= (encode-repeating-key "repeating-key-test" "ICE")

           "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f"))

  )

)

(deftest single-cipher

  (let [single-cipher-text
              (->> "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"
                   (map #(.indexOf hex-chars %))
                   (mapcat base16-to-bits)
                   (partition 8)
                   (map reverse)
                   (map #(map bit-shift-left % (range)))
                   (map #(reduce + %))
                   )

              test-range (doall (range 256))
              result (->> test-range
                          (map #(xor-with-offset single-cipher-text %))
                          (filter is-letters?)
                          )

              ]
          (testing "Sleazing a single cipher"
            (is (= result '("Cooking MC's like a pound of bacon"))))

          )

)

(deftest hamming-distance

  (testing "Hamming example on strings"
    (is (= (ascii-hamming-distance "this is a test" "wokka wokka!!!") 37
    ))
  )
)

(deftest break-vigenere

  (testing "Example breaking vignere"
    (println
      (let [data (slurp-base64-to-bytes "repeating-key")
            keysize (get-keysize 41 data)
            lines (->> data
                       (partition keysize)
                       (apply map list))]

        (letfn [(process [line]
                  (->> (range 256)
                       (map #(xor-with-offset line %))
                       (sort-by letter-percentage >)
                       (first)
                       ))

                ]

          (let [result (->> (map process lines)
                            (apply mapcat list)
                            (apply str)

                            )]


            (->> result
                 (map int)
                 (map bit-xor data)
                 (map char)
                 (take keysize)
                 (apply str)
                 )
            result

            (is (= (apply str (take 30 result)) "I'm back and I'm ringin' the b"))
            )

          )))
    )


)

