(ns yuhrao.midi-hub.core
  (:require [yuhrao.midi-hub.lib.db :as mh.db]
            [yuhrao.midi-hub.lib.openmidi :as openmidi]))

(defn list-brands
  "You can pass an option map with Honey SQL DSL"
  ([] (mh.db/list-brands))
  ([hsql-dql]
   (mh.db/list-brands hsql-dql)))

(defn list-devices
  "You can pass an option map with Honey SQL DSL"
  ([] (mh.db/list-devices))
  ([hsql-dql]
   (mh.db/list-devices hsql-dql)))

(defn sync-openmidi []
  (openmidi/import-specs))
