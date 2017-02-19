(ns status-im.components.sortable-list-view
  (:require [reagent.core :as r]
            [status-im.components.react :refer [view
                                                touchable-highlight
                                                list-item]]))

(def sortable-list-view-class (r/adapt-react-class (js/require "react-native-sortable-listview")))

(defn sortable-list-view [{:keys [onRowMoved renderRow] :as props}]
  [sortable-list-view-class
   (assoc props :onRowMoved #(onRowMoved (js->clj % :keywordize-keys true))
                :renderRow #(renderRow (js->clj % :keywordize-keys true)))])

(defn touchable [inner]
  (let [this (r/current-component)]
    [touchable-highlight (merge (js->clj (.-props this)) {:delayLongPress 300})
     [view
      inner]]))

(defn sortable-item [inner]
  (list-item [touchable inner]))