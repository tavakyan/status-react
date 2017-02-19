(ns status-im.new-group.views.reorder-groups
  (:require-macros [status-im.utils.views :refer [defview]])
  (:require [re-frame.core :refer [dispatch dispatch-sync]]
            [status-im.components.react :refer [view
                                                text
                                                icon
                                                touchable-highlight
                                                list-item]]
            [status-im.components.confirm-button :refer [confirm-button]]
            [status-im.components.status-bar :refer [status-bar]]
            [status-im.components.toolbar.view :refer [toolbar]]
            [status-im.components.toolbar.styles :as tst]
            [status-im.components.sortable-list-view :refer [sortable-list-view sortable-item]]
            [status-im.utils.listview :refer [to-datasource]]
            [status-im.utils.platform :refer [android?]]
            [status-im.new-group.styles :as st]
            [status-im.i18n :refer [label label-pluralize]]
            [reagent.core :as r]))

(defn toolbar-view []
  [toolbar {:style (merge tst/toolbar-inner st/toolbar-view)
            :title (label :t/reorder-groups)}])

(defn group-item [{:keys [name contacts] :as group}]
  (let [cnt (count contacts)]
    [view st/order-item-container
     [view st/order-item-inner-container
      [text {:style st/order-item-label}
       name]
      [view {:flex 1}]
      [view st/order-item-icon
       [icon :grab_gray]]
      #_[text {:style {:padding-left 8}}
         (str cnt " " (label-pluralize cnt :t/contact-s))]]
     [view st/order-item-separator]]))

(defview reorder-groups []
  [groups [:get :contact-groups]
   order  [:get :groups-order]]
  (let [this (r/current-component)]
    [view st/reorder-groups-container
     [status-bar (when android? {:type :gray})]
     [toolbar-view]
     [view {:flex 1}
      [sortable-list-view
       {:data       groups
        :order      order
        :onRowMoved #(do (dispatch-sync [:change-group-order (:from %) (:to %)])
                         (.forceUpdate this))
        :renderRow  (fn [row]
                      (sortable-item [group-item row]))}]]
     [confirm-button (label :t/save) #(dispatch [:save-group-order])]]))
