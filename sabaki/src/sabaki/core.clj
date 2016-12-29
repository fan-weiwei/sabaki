(ns sabaki.core
  (require [clojure.core.async
            :as a
            :refer [>! <! >!! <!! go chan buffer close! thread
                    alts! alts!! timeout]])
  (require [sabaki.encoding :refer :all])
  (require [sabaki.aes :refer :all]))


(defn word-to-hex [word]
  (->> word
       (map byte-to-bits)
       (map bits-to-hexString)
       (apply str)
       )
)

(defn -main "高级加密标准" [& args]
  (println
  (let [key (->> "YELLOW SUBMARINE"
                       (map int))]

    (->> (slurp-base64-to-bytes "ecb-encrypted")
               (aes-decrypt key)
               (map bytes-to-string)
               (apply str))

        ))

)


