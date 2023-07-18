(ns br.dev.yuhri.logger.log
  (:require [com.brunobonacci.mulog :as u]))

(defmacro log
  ([event-name level message]
   (log event-name level message nil))
  ([event-name level message data]
   (if data
     `(u/log ~event-name :level ~level :message ~message :data ~data)
     `(u/log ~event-name :level ~level :message ~message))))
