(ns yuhrao.data-cloak.core.string
  (:require [yuhrao.data-cloak.string :as dc.impl.string]))

(defn offset
  "Given a string, obscure all the string content from `start`+`start-offset` to `end`-`end-offset`>
  opts:
  - `:start`: amount of chars at the beginning of the string to not be obscured
  - `:end`: amount of chars at the end of the string to not be obscured

  ```clojure
  (dc.string/offset \"1234567890\" {:start 3 :end 4}) ;;=> \"123***4890\")
  ```

  "
  [s opts]
  (dc.impl.string/offset s opts))

(defn symmetric-offset
  "Same as `offset` but `:start` and `:end` are equal"
  [s n]
  (dc.impl.string/symmetric-offset s n))

(defn all
  "Obscure the whole string. Useful for passwords

  Example:
  ```clojure
  (dc.string/all \"1234567890\") ;;=> \"**********\"
  ```"
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
  such as `()+` and white spaces

  Examples:
  - +55 (11) 91234-1234 -> 55119****1234
  - (11) 91234-1234 -> 119****1234
  - 91234-1234 -> *****1234"
  [s]
  (dc.impl.string/phone s))
