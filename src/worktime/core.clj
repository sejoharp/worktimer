#!/usr/bin/env bb

(require '[clojure.tools.cli :refer [parse-opts]]
         '[clojure.string :as string]
         '[clojure.java.io :as jio]
         '[clojure.pprint :as pretty])

(def home-path (System/getenv "HOME"))
(def version "active-development")
(def file-path (str home-path "/workingtimes-test"))
(def now (java.time.ZonedDateTime/now))
(def now-time (->> (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss")
                   (#(.format now %))))
(def now-date (->> (java.time.format.DateTimeFormatter/ofPattern "YYYY-MM-dd")
                   (#(.format now %))))

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

(defn main []
  (let [argument (first *command-line-args*)]
    (case argument
      "start" (start! file-path)
      "stop" (stop! file-path)
      "status" (status file-path)
      "version" (println (str "worktimer version: " version))
      "help" (usage)
      (usage)
      )))

(main)
