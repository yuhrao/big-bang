---
sidebar_label: migration
title: yuhrao.database.migration
toc_min_heading_level: 2
toc_max_heading_level: 4
custom_edit_url:
---

This namespace is deprecated. Use abogoyavlensky/automigrate instead.

*deprecated*





### rollback\-migration {#rollback-migration}
``` clojure
(rollback-migration db migration-opts)
```

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/database/migration.clj#L16-L19">Source</a></sub></p>

### run\-migrations {#run-migrations}
``` clojure
(run-migrations migrations)
(run-migrations migrations {:keys [skip-main?], :as _opts})
```

<p><sub><a href="https://github.com/yuhrao/big-bang/blob/main//src/yuhrao/database/migration.clj#L33-L42">Source</a></sub></p>
