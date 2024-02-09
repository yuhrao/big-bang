(ns br.dev.yuhri.rest.midi-hub.schema
  (:require [br.dev.yuhri.rest.schema :as schema]))

(def get-by-id-params
  [:map
   [:id schema/UUID]])

;; TODO: put param mapper map
(def cc-spec
  [:map
   [:description {:optional true} :string]
   [:max {:optional true} :int]
   [:min {:optional true} :int]
   [:name {:optional true} :string]
   [:value {:optional true} :int]
   [:type {:optional true} :string]])

(def device-midi-spec
  [:map
   [:midi_clock {:optional true} :boolean]
   [:midi_channel {:optional true} [:map [:instructions
                                          {:optional true}
                                          :string]]]
   [:cc {:optional true} [:* cc-spec]]
   [:pc {:optional true} [:map [:description {:optional true} :string]]]])

(def device-io-spec
  [:map
   [:midi_in {:optional true} :string]
   [:midi-thru {:optional true} :boolean]
   [:phantom-power {:optional true} :boolean]])

(def device-metadata
  [:map
   [:missing_data {:optional true} [:* :string]]])

(def device
  [:map
   [:id schema/UUID]
   [:brand_id schema/UUID]

   [:description {:optional true} :string]
   [:device_name :string]
   [:value :string]

   [:midi_specs {:optional true} device-midi-spec]
   [:io_specs {:optional true} device-io-spec]
   [:metadata {:optional true} device-metadata]

   [:created_at schema/DateTime]
   [:updated_at schema/DateTime]])
