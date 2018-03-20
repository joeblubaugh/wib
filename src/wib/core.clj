(ns wib.core
  (:require [quil.core :as q]
            [clojure.math.numeric-tower :as math]))

(defn dev [target value]
  (math/abs (- target value)))

(defn clamp [value min max]
  (cond
    (< value min) min
    (> value max) max
    :else value
    )
  )

(defn pleasantness [args]
  "Returns a number on [-1 1] where: 
  -1 is 'unpleasant-cold',
  0 is 'pleasant'
  1 is 'unpleasant-hot'"
  (let [{high :high
        low :low
        w :wind
        p :precip} (:options args)
        hFactor 0.05
        idealH 76
        lFactor 0.025
        idealL 60
        wFactor 0.05
        idealW 2
        pFactor 0.2
        idealP 0
        hDiff (- high idealH)
        lDiff (- low idealL )
        wDiff (- w idealW)
        pDiff (- p idealP)
        ]
    (clamp 
      (+ (* hFactor hDiff)
         (* lFactor lDiff)
         (* wFactor (* -1 wDiff)) ; high wind makes it colder
         (* pFactor (* -1 (/ pDiff 100))) ; precip makes it colder
         )
      -1 1)))

(defn setup [args config]
  "Takes args and config maps and sets up the sketch"
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 30)
  ; Set color mode to HSB (HSV) instead of default RGB.
  (q/color-mode :hsb 360 100 100 1.0)
  ; Create initial state from config
  (let [pl (pleasantness args)
        palette (cond 
                  (< pl -0.4) (:cold-palette config)
                  (> pl 0.4) (:hot-palette config)
                  :else (:pleasant-palette config))]
    (-> {}
        ; constants
        (assoc :palette palette)
        (assoc :variation (:variation config))
        (assoc :maxx (:maxx config))
        (assoc :maxy (:maxy config))
        (assoc :squareinc (:squareinc config))
        (assoc :spacing (:spacing config))
        (assoc :sidelen (:sidelen config))
        ; varies
        (assoc :color (rand-nth palette))
        (assoc :x 0)
        (assoc :y 0)
        (assoc :up (rand-nth [true false]))
        ))
  )

(defn preset [args config]
  "Applies config and returns a no-arg 'setup' function"
  (partial setup args config)
)


(defn update-state [state]
  "Advances the state of the sketch by moving through the grid and exiting when done."
  ; weirdly, need to do maxx - 2 because a draw is already queued up
  (if (and (= (:y state) (- (:maxy state) 1)) (= (:x state) (- (:maxx state) 2))) (do 
                                           (println "done")
                                           (q/no-loop)))
  (-> state
      (assoc :color (rand-nth [(:color state) (:color state) (rand-nth (:palette state))]))
      (assoc :x (mod (+ (:x state) 1) (:maxx state)))
      (assoc :y (cond (= (:x state) (- (:maxx state) 1)) (+ (:y state) 1) :else (:y state)))
      (assoc :up (rand-nth [true false]))
      )
  )

(defn randaround [value variance]
  (+ value (- (rand variance) (/ variance 2))))

(defn draw [state]
  "Draws one square-triangle per iteration."
  
  (let [[h s b] (:color state)
        {vh :h vs :s vb :b} (:variation state)
        color [(randaround h vh) (randaround s vs) (randaround b vb)]
        {squareinc :squareinc 
         spacing :spacing 
         sidelen :sidelen 
         up :up} state
        x1 (+ (* (:x state) squareinc) spacing) 
        y1 (+ (* (:y state) squareinc) spacing) 
        x2 (+ x1 sidelen)
        y2 (+ y1 sidelen)
        x3 (+ x1 (cond up sidelen :else 0))
        y3 (+ y1 (cond up 0 :else sidelen))
        x4 (+ x1 (cond up 0 :else sidelen))
        y4 (+ y1 (cond up sidelen :else 0))
        ]
         
        ; "Real" triangle
        (apply q/stroke color)
        (q/stroke-weight 1)
        (apply q/fill (conj color 75))
        (q/triangle 
          x1 y1 x2 y2 x3 y3)

        ))

