---
sidebar_label: core
title: yuhrao.logger.core
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### debug {#debug}
``` clojure
(debug event-name message)
(debug event-name message data)
```


Log at debug level
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/logger/core.clj#L32-L37">Source</a></sub></p>

### error {#error}
``` clojure
(error event-name message)
(error event-name message data)
```


Log at error level
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/logger/core.clj#L53-L58">Source</a></sub></p>

### info {#info}
``` clojure
(info event-name message)
(info event-name message data)
```


Log at info level
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/logger/core.clj#L39-L44">Source</a></sub></p>

### log {#log}
``` clojure
(log level event-name message)
(log level event-name message data)
```


Log at error level
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/logger/core.clj#L25-L30">Source</a></sub></p>

### restart\! {#restart-BANG-}
``` clojure
(restart!)
```


Restart the logger. It will start again with the same configuration.
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/logger/core.clj#L20-L23">Source</a></sub></p>

### set\-global\-context {#set-global-context}
``` clojure
(set-global-context context-map)
```


Set context for all logs
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/logger/core.clj#L183-L186">Source</a></sub></p>

### setup\! {#setup-BANG-}
``` clojure
(setup! opts)
```


Start and configure the logger
  opts:
  - `:min-level`: minimum level to be logged
  - `:publishers`: a map with publishers to put logs into. See `publishers` section in README
  - `:obscurer`: an obscurer function. Check out `yuhrao.data-cloak.core.map` namespace. **It obscures ONLY FIELDS IN LOG'S DATA**
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/logger/core.clj#L6-L13">Source</a></sub></p>

### stop\! {#stop-BANG-}
``` clojure
(stop!)
```


Stop the logger. No more logs will be published.
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/logger/core.clj#L15-L18">Source</a></sub></p>

### trace {#trace}
``` clojure
(trace event-name trace-opts & body)
```


Traces the execution of an operation with the outcome and the time
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
  {:mulog/trace-id #mulog/flake "4VIKxhMPB2eS0uc1EV9M9a5G7MYn3TMs",
   :mulog/event-name :your-ns/availability,
   :mulog/timestamp 1586804894278,
   :mulog/duration 253303600,
   :mulog/namespace "your-ns",
   :mulog/outcome :ok,
   :mulog/root-trace #mulog/flake "4VILF82cx_mFKlbKN-PUTezsRdsn8XOY",
   :mulog/parent-trace #mulog/flake "4VILL47ifjeHTaaG3kAWtZoELvk9AGY9",
   :order "34896-34556",
   :product-id "2345-23-545",
   :user "709-6567567"}
  ```

  Note the `:mulog/duration` and `:mulog/outcome` reporting
  respectively the duration of the execution of `product-availablity`
  in **nanoseconds** as well as the outcome (`:ok` or `:error`). If an
  exception is raised within the body an additional field is added
  `:exception` with the exception raised.

  The `:pairs` present in the vector are added in the event, but they
  are not propagated to nested traces, use [`with-context`](#with-context) for that.

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
                       :etag (get-in r [:headers "etag"])})
    (product-availability product-id))
  ```

  Will produce an event as follow:

  ``` Clojure
  {:mulog/trace-id #mulog/flake "4VIKxhMPB2eS0uc1EV9M9a5G7MYn3TMs",
   :mulog/event-name :your-ns/availability,
   :mulog/timestamp 1586804894278,
   :mulog/duration 253303600,
   :mulog/namespace "your-ns",
   :mulog/outcome :ok,
   :mulog/root-trace #mulog/flake "4VILF82cx_mFKlbKN-PUTezsRdsn8XOY",
   :mulog/parent-trace #mulog/flake "4VILL47ifjeHTaaG3kAWtZoELvk9AGY9",
   :order "34896-34556",
   :product-id "2345-23-545",
   :user "709-6567567",
   :http-status 200,
   :etag "1dfb-2686-4cba2686fb8b1"}
  ```

  Note that in addition to the pairs like in the previous example
  this one contains `:http-status` and `:etag` which where extracted
  from the http response of `product-availability` evaluation.

  Should the execution of the `capture` function fail for any reason
  the pair will be added to this trace with `:mulog/capture :error`
  to signal the execution error.

  

*macro*

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/logger/core.clj#L66-L181">Source</a></sub></p>

### warn {#warn}
``` clojure
(warn event-name message)
(warn event-name message data)
```


Log at warn level
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/logger/core.clj#L46-L51">Source</a></sub></p>

### with\-context {#with-context}
``` clojure
(with-context context-map & body)
```


Add local context to logs

*macro*

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/logger/core.clj#L60-L64">Source</a></sub></p>
