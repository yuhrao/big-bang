(ns br.dev.yuhri.data-cloak.core.string
  (:require [br.dev.yuhri.data-cloak.string :as dc.impl.string]))

(defn offset
  "Given a string, obscure all the string content from start+<start-offset> to end-<end-offset>
  opts:
  - `:start`: amount of chars at the beginning of the string to not be obscured
  - `:end`: amount of chars at the end of the string to not be obscured"
  [s opts]
  (dc.impl.string/offset s opts))

(defn symmetric-offset
  "Same as `offset` but `:start` and `:end` are equal"
  [s n]
  (dc.impl.string/symmetric-offset s n))

(defn all
  "Obscure the whole string. Useful for passwords"
  [s]
  (dc.impl.string/all s))

(defn email
  "Obscure an email. Examples:
  - some.email@gmail.com -> so******il@gmail.com
  - small@gmail.com -> s****@gmail.com
  - bob@gmail.com -> b**@gmail.com"
  [s]
  (dc.impl.string/email s))

(defn phone
  "Obscure a phone number. It removes phone number's specific special characters
  such as `()+` and white spaces"
  [s]
  (dc.impl.string/phone s))
