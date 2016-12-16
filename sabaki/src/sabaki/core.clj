(ns sabaki.core
  (require [sabaki.encoding :refer :all])
  (require [sabaki.ops :refer :all]))

(def cipher-text "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736")

(def test-range (doall (range 256)))

(defn -main [& args]
  (->> test-range
       (map #(xor-with-offset cipher-text %))
       (filter is-letters?)
       (print)
       )
  )

