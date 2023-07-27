(ns br.dev.yuhri.logger.interface
  (:require [br.dev.yuhri.logger.logger :as i.logger]
            [br.dev.yuhri.logger.log :as i.log]
            [com.brunobonacci.mulog :as u]))

(defn setup!
  "Start and configure the logger"
  [opts]
  (i.logger/setup! opts))

(defn stop! []
  (i.logger/stop!))

(defn restart! []
  (i.logger/restart!))

(defn log
  "Log at error level"
  ([level event-name message]
   (log level event-name message nil))
  ([level event-name message data]
   (i.log/log event-name level message data)))

(defn debug
  "Log at debug level"
  ([event-name message]
   (debug event-name message nil))
  ([event-name message data]
   (log :debug event-name message data)))

(defn info
  "Log at info level"
  ([event-name message]
   (info event-name message nil))
  ([event-name message data]
   (log :info event-name message data)))

(defn warn
  "Log at warn level"
  ([event-name message]
   (warn event-name message nil))
  ([event-name message data]
   (log :warn event-name message data)))

(defn error
  "Log at error level"
  ([event-name message]
   (error event-name message nil))
  ([event-name message data]
   (log :error event-name message data)))
