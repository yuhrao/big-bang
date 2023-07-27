# Database

Iteract with databases

## TOC

<!-- TOC -->
* [Database](#database)
  * [TOC](#toc)
  * [Libs](#libs)
  * [How to add a new database](#how-to-add-a-new-database)
  * [How to create migrations](#how-to-create-migrations)
<!-- TOC -->

## Libs

| Lib       | Description                |
|-----------|----------------------------|
| Ragtime   | Database Migrations        |
| Next JDBC | Execute SQL statements     |
| Honey SQL | Data driven SQL statements |

## How to add a new database

1. Create a folder into `resources/migration` with your database name (e.g. `resources/migration/my-db`)
2. In `br.dev.yuhri.database.migration` namespace, add your default config in `default-opts` map as shown below

```clojure
(def ^:private default-opts {:main {:migrations "migrations/main"}
                             ...
                             :my-db {:migrations "migrations/my-db"}})
```

2. Now you're able to run the migration in your project putting the rest of the configuration:

YOU ALWAYS NEED TO CONFIGURE THE MAIN DATABASE
ENSURE THAT THE KEY YOU'LL USE IN YOUR OPTS IS THE SAME YOU USED IN `default-opts`
```clojure
(let [opts {:main  {:datastore {:dbtype   "postgres"
                                :dbname   "psql"
                                :host     "localhost"
                                :port     5432
                                :user     "app"
                                :password "app"}}
            :my-db {:datastore {:dbtype   "postgres"
                                :dbname   "other-db"
                                :host     "other.postgres.db"
                                :port     5432
                                :user     "app"
                                :password "app"}}}]
  (db/run-migrations opts))
```

## How to create migrations

1. Create a file in this componen's resource inside `migrations/<your-db>`. It should be composed by the timestamp
(format: `yyyyMMddhhmm`) and a short description (e.g. `202101021203_create_new_table.edn`)
> For now there's a babashka task to get the timestamp to build your file name

2. The content should follow the standard below:
Check this [ragtime guide](https://github.com/weavejester/ragtime/wiki/Getting-Started) for more detailed docs
```clojure
{:up   ["<sql-statement1>"
        "<sql-statement2>"]
 :down ["<sql-statement3>"
        "<sql-statement4>"]}
```
