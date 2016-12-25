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

