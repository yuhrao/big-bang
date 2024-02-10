---
sidebar_label: string
title: yuhrao.data-cloak.core.string
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### all {#all}
``` clojure
(all s)
```


Obscure the whole string. Useful for passwords
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/data_cloak/core/string.clj#L17-L20">Source</a></sub></p>

### email {#email}
``` clojure
(email s)
```


Obscure an email. Examples:
  - some.email@gmail.com -> so******il@gmail.com
  - small@gmail.com -> s****@gmail.com
  - bob@gmail.com -> b**@gmail.com
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/data_cloak/core/string.clj#L22-L28">Source</a></sub></p>

### offset {#offset}
``` clojure
(offset s opts)
```


Given a string, obscure all the string content from `start`+`start-offset` to `end`-`end-offset>
  opts:
  - `:start`: amount of chars at the beginning of the string to not be obscured
  - `:end`: amount of chars at the end of the string to not be obscured
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/data_cloak/core/string.clj#L4-L10">Source</a></sub></p>

### phone {#phone}
``` clojure
(phone s)
```


Obscure a phone number. It removes phone number's specific special characters
  such as `()+` and white spaces
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/data_cloak/core/string.clj#L30-L34">Source</a></sub></p>

### symmetric\-offset {#symmetric-offset}
``` clojure
(symmetric-offset s n)
```


Same as [`offset`](#offset) but `:start` and `:end` are equal
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/data_cloak/core/string.clj#L12-L15">Source</a></sub></p>
