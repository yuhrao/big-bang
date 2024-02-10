(ns yuhrao.logger.core
  (:require [yuhrao.logger.logger :as i.logger]
            [yuhrao.logger.log :as i.log]
            [com.brunobonacci.mulog :as u]))

(defn setup!
  "Start and configure the logger
  opts:
  - `:min-level`: minimum level to be logged
  - `:publishers`: a map with publishers to put logs into. See `publishers` section in README
  - `:obscurer`: an obscurer function. Check out `yuhrao.data-cloak.core.map` namespace. **It obscures ONLY FIELDS IN LOG'S DATA**"
  [opts]
  (i.logger/setup! opts))

(defn stop!
  "Stop the logger. No more logs will be published."
  []
  (i.logger/stop!))

(defn restart!
  "Restart the logger. It will start again with the same configuration."
  []
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

(defmacro with-context
  "Add local context to logs"
  {:style/indent 1}
  [context-map & body]
  `(u/with-context ~context-map ~@body))

(defmacro trace
"Traces the execution of an operation with the outcome and the time
  taken in nanoseconds.

  ### Track duration and outcome (errors)

  ***μ/trace*** will generate a trace object which can be understood by
  distributed tracing systems.

  It computes the duration in nanoseconds of the current trace/span
  and it links via the context to the parent trace and root traces.

  It tracks the `:outcome` of the evaluation of the `body`.  If the
  evaluation it throws an exception `:outcome` will be `:error`
  otherwise it will be `:ok`

  The trace information will be tracked across function calls as long as
  the execution is in the same thread. If the execution spans more threads
  or more processes the context must be passed forward.

  Example of usage:

  ``` Clojure
  (u/trace ::availability
    [:product-id product-id, :order order-id, :user user-id]
    (product-availability product-id))
  ```

  Will produce an event as follow:

  ``` Clojure
  {:mulog/trace-id #mulog/flake \"4VIKxhMPB2eS0uc1EV9M9a5G7MYn3TMs\",
   :mulog/event-name :your-ns/availability,
   :mulog/timestamp 1586804894278,
   :mulog/duration 253303600,
   :mulog/namespace \"your-ns\",
   :mulog/outcome :ok,
   :mulog/root-trace #mulog/flake \"4VILF82cx_mFKlbKN-PUTezsRdsn8XOY\",
   :mulog/parent-trace #mulog/flake \"4VILL47ifjeHTaaG3kAWtZoELvk9AGY9\",
   :order \"34896-34556\",
   :product-id \"2345-23-545\",
   :user \"709-6567567\"}
  ```

  Note the `:mulog/duration` and `:mulog/outcome` reporting
  respectively the duration of the execution of `product-availablity`
  in **nanoseconds** as well as the outcome (`:ok` or `:error`). If an
  exception is raised within the body an additional field is added
  `:exception` with the exception raised.

  The `:pairs` present in the vector are added in the event, but they
  are not propagated to nested traces, use `with-context` for that.

  Finally, `:mulog/trace-id`, `:mulog/parent-trace` and
  `:mulog/root-trace` identify respectively this trace, the outer
  trace wrapping this trace if present otherwise `nil` and the
  `:mulog/root-trace` is the outer-most trace with not parents.  Keep
  in mind that *parent-trace* and *root-trace* might come from another
  system and they are propagated by the context.

  ### Capture evaluation result

  Sometimes it is useful to add to the trace pairs which come from the
  result of the body's evaluation. For example to capture the http
  response status or other valuable metrics from the response.
  ***μ/trace*** offers the possibility to pass a function to capture
  such info from the evaluation result.
  To achieve this, instead of passing a simple vector of pairs
  you need to provide a map which contains a `:capture` function
  in addition to the `:pairs`.

  The `capture` function is a function which takes one argument,
  *the result* of the evaluation and returns a map of key-value pairs
  which need to be added to the trace. The `capture` function will only
  run when the `:mulog/outcome :ok`

  Example of usage:

  ``` Clojure
  (u/trace ::availability
    {:pairs [:product-id product-id, :order order-id, :user user-id]
     :capture (fn [r] {:http-status (:status r)
                       :etag (get-in r [:headers \"etag\"])})
    (product-availability product-id))
  ```

  Will produce an event as follow:

  ``` Clojure
  {:mulog/trace-id #mulog/flake \"4VIKxhMPB2eS0uc1EV9M9a5G7MYn3TMs\",
   :mulog/event-name :your-ns/availability,
   :mulog/timestamp 1586804894278,
   :mulog/duration 253303600,
   :mulog/namespace \"your-ns\",
   :mulog/outcome :ok,
   :mulog/root-trace #mulog/flake \"4VILF82cx_mFKlbKN-PUTezsRdsn8XOY\",
   :mulog/parent-trace #mulog/flake \"4VILL47ifjeHTaaG3kAWtZoELvk9AGY9\",
   :order \"34896-34556\",
   :product-id \"2345-23-545\",
   :user \"709-6567567\",
   :http-status 200,
   :etag \"1dfb-2686-4cba2686fb8b1\"}
  ```

  Note that in addition to the pairs like in the previous example
  this one contains `:http-status` and `:etag` which where extracted
  from the http response of `product-availability` evaluation.

  Should the execution of the `capture` function fail for any reason
  the pair will be added to this trace with `:mulog/capture :error`
  to signal the execution error.

  "
  {:style/indent 1}
  [event-name trace-opts & body]
  `(i.log/trace ~event-name ~trace-opts ~@body))

(defn set-global-context
  "Set context for all logs"
  [context-map]
  (u/set-global-context! context-map))
