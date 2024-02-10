---
sidebar_label: core
title: yuhrao.database.core
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---





### create\-pool {#create-pool}
``` clojure
(create-pool db-spec)
```


Creates a connection pool from a next.jdbc db-spec
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/database/core.clj#L34-L37">Source</a></sub></p>

### execute\! {#execute-BANG-}
``` clojure
(execute! db-spec sql)
```


Executes a sql statement.
  `sql` can be a string or a honeysql query
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/database/core.clj#L28-L32">Source</a></sub></p>

### insert\! {#insert-BANG-}
``` clojure
(insert! db-spec table-name entity)
```


Inserts an entity into the table
<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/database/core.clj#L23-L26">Source</a></sub></p>

### rollback\-migration {#rollback-migration}
``` clojure
(rollback-migration db migration-opts)
```


Rollback a migration in the migrations map.
  DEPRECATED USE [abogoyavlensky/automigrate](https://github.com/abogoyavlensky/automigrate) INSTEAD

*deprecated*

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/database/core.clj#L16-L21">Source</a></sub></p>

### run\-migrations {#run-migrations}
``` clojure
(run-migrations migrations)
(run-migrations migrations opts)
```


Run all migrations in the migrations map.
  DEPRECATED USE [abogoyavlensky/automigrate](https://github.com/abogoyavlensky/automigrate) INSTEAD

*deprecated*

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/database/core.clj#L8-L14">Source</a></sub></p>
