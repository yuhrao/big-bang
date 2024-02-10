# Database

Iteract with databases

<!-- markdown-toc start - Don't edit this section. Run M-x markdown-toc-refresh-toc -->
**Table of Contents**

- [Database](#database)
    - [Libs](#libs)
    - [How to add a new database](#how-to-add-a-new-database)
    - [How to create migrations](#how-to-create-migrations)

<!-- markdown-toc end -->


## Libs

| Lib       | Description                |
|-----------|----------------------------|
| Ragtime   | Database Migrations        |
| Next JDBC | Execute SQL statements     |
| Honey SQL | Data driven SQL statements |

## How to add a new database

```clojure
(let [db-spec-1 {:datastore {:dbtype   "postgres"
                             :dbname   "psql"
                             :host     "localhost"
                             :port     5432
                             :user     "app"
                             :password "app"}}
      db-spec-2 {:datastore {:dbtype   "postgres"
                             :dbname   "other-db"
                             :host     "other.postgres.db"
                             :port     5432
                             :user     "app"
                             :password "app"}}]
  ...)
```

## How to create migrations

You can follow instructions in [automigrate](https://github.com/abogoyavlensky/automigrate)

```clojure
{:aliases {:your-alias {:ns-default automigrate.core
                        :exec-args  {:models-file    "resources/db/models.edn"
                                     :migrations-dir "resources/db/migrations"
                                     :jdbc-url       "jdbc:postgresql://localhost:5432/mydb?user=myuser&password=secret"}}}}
```

Run migrations following automigrate instructions

```shell
# 1- Create generate migration files
clojure -X:migrations make
## With Custom Name
clojure -X:migrations make :name <name>

# 2- Execute migrations

# Dev
clojure -X:migrations migrate
# Prod
clojure -X:migrations migrate :jdbc-url $DATABASE_URL
```
