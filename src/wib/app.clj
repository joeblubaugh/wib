(ns wib.app
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [wib.core :as core]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def cli-flags
  [["-h" "--high FAHR" "Daily High"
    :default 80
    :parse-fn #(Float/parseFloat %)]
   ["-l" "--low FAHR" "Daily Low"
    :default 40
    :parse-fn #(Float/parseFloat %)]
   ["-p" "--precip PRECIP" "Chance of precipitation"
    :default 0.2
    :parse-fn #(Float/parseFloat %)]
   ["-w" "--wind WIND" "Wind speed"
    :default 1.0
    :parse-fn #(Float/parseFloat %)]
   ])

; Program config, changed by developer
(def config
  (let [
        sidelen 180
        squareinc 200
        spacing (/ (- squareinc sidelen) 2)
        ]
    {:size 1400
     ; palettes in HSB
     :pleasant-palette [
                        [200 77 90]
                        [160 75 80] ;sea-foam green
                        [129 66 62]
                        [129 66 96]
                        ]
     :hot-palette [
                    [335 81 92]
                    [0 97 75]
                    [16 92 96]
                    [49 82 95]
                    ]

     :cold-palette [
                    [290 72 73]
                    [261 58 73]
                    [192 72 94]
                    [216 56 64]
                    ]

     :sidelen sidelen
     :squareinc squareinc
     :spacing spacing
     :maxx 7 
     :maxy 5
     ; How much randomness to apply around a color number
     :variation {:h 10 :s 5 :b 5}
     } 
    ))

(defn -main [& args]
  (let [clargs (parse-opts args cli-flags)]

    (q/sketch
      :title "The Weather in Brooklyn"
      :setup (core/preset clargs config)
      :update core/update-state
      :draw core/draw
      :size [(:size config) (:size config)]
      :middleware [m/fun-mode]
      )))
