(ns sabaki.test
  (:require [clojure.test :refer :all]
            [sabaki.core :refer :all]
            [sabaki.encoding :refer :all]
            [sabaki.ops :refer :all]))

(deftest hex

  (def hex-string "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d")
  (def irl-string "I'm killing your brain like a poisonous mushroom")
  (def base64-string "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t")

  (testing "Test hex-string to base64"
    (def result (bits-to-base64String (hexstring-to-bits hex-string)))
    (is (= result base64-string)))



)
