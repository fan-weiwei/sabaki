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

  (def hex-string "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")
  (def irl-string "I'm killing your brain like a poisonous mushroom")
  (def base64-string "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t")

  (testing "Test hex-string to base64"
    (def result (bits-to-base64String (hexstring-to-bits hex-string)))
    (is (= result base64-string)))
)

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

  (def single-cipher-text "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736")
  (def test-range (doall (range 256)))

  (def result
      (->> test-range
          (map #(xor-with-offset single-cipher-text %))
          (filter is-letters?)
      )
  )

  (testing "Sleazing a single cipher"
    (is (= result '("Cooking MC's like a pound of bacon"))))

)

(deftest single-cipher-file

         ; Get lines
         (def lines (doall
                      (->> (slurp "single-cipher")
                           (clojure.string/split-lines))))

         ; Define break for single line
         (defn process [cipher-text channel]
           (go (let [result (sleaze-single-xor cipher-text)]
                 (if (not-empty result) (>! channel result))))
           )

         ; Make channels for each line
         (let [ chans (mapv #(vector % (chan)) lines)
               chans-only (mapv second chans) ]

           (doseq [pair chans] (apply process pair))

           (let [[v p] (alts!! chans-only)]
                ;(println "Result: " v)
                (testing "Sleazing a single cipher"
                  (is (= v "Now that the party is jumping\n")))


           )
         )
)

(deftest hamming-distance

  (testing "Hamming example on strings"
    (is (= (ascii-hamming-distance "this is a test" "wokka wokka!!!") 37
    ))


  )

)

