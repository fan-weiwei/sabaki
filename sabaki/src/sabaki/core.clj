(ns sabaki.core
  (require [sabaki.encoding :refer :all])
  (require [sabaki.ops :refer :all]))


(defn sleaze-single-cipher []
  (def result
    (->> (slurp "single-cipher")
         (clojure.string/split-lines)
         (mapcat sleaze-single-xor)
         (filter #(not= (count %) 0))
         )
    )

  (println result)
)

(defn -main [& args]

  (time (sleaze-single-cipher))

)


