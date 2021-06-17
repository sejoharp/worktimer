(ns worktimer.core
  (:require [clojure.string :as string]
            [clojure.java.io :as jio]
            [clojure.pprint :as pretty]
            [worktimer.config :as config])
  (:gen-class))

(defn now [] (java.time.ZonedDateTime/now))
(defn now-time [] (->> (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss")
                   (#(.format (now) %))))
(defn now-date [] (->> (java.time.format.DateTimeFormatter/ofPattern "YYYY-MM-dd")
                   (#(.format (now) %))))

(defn status-line [status]
  {:date   now-date
   :time   now-time
   :status status})

(defn read-work-timings [path]
  (->> path
       slurp
       read-string
       vec))

(defn status [path]
  (pretty/pprint (read-work-timings path)))

(defn write! [work-timings path]
  (pretty/pprint
    work-timings
    (jio/writer path)))

(defn usage []
  (->> ["This programs tracks worked time."
        ""
        "Usage: worktimer action"
        ""
        "Actions:"
        "  start    Start working (persist starting time)"
        "  stop     Stop working (persist stoping time)"
        "  status   show the logged periods"
        "  version   show corresponding commit id"
        "  help     show this"
        ""]
       (string/join \newline)
       println))

(defn start! [path]
  (let [line (status-line :start)
        work-timings (read-work-timings path)
        work-timings-with-line (conj work-timings line)]
    (write! work-timings-with-line path)))

(defn stop! [path]
  (let [line (status-line :stop)
        work-timings (read-work-timings path)
        work-timings-with-line (conj work-timings line)]
    (write! work-timings-with-line path)))

(defn -main [& args]
  (let [argument (first args)]
    (case argument
      "start" (start! config/file-path)
      "stop" (stop! config/file-path)
      "status" (status config/file-path)
      "version" (println (str "worktimer version: " config/version))
      "help" (usage)
      (usage)
      )))
