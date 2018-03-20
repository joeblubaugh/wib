(defproject wib "0.1.0-SNAPSHOT"
  :description "The Weather in Brooklyn logo generation"
  :url "http://joeblu.com/projects/weather-in-brooklyn"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/tools.cli "0.3.5"]
                 [quil "2.6.0"]]
  :aot [wib.app wib.core]
  :main wib.app
  )
