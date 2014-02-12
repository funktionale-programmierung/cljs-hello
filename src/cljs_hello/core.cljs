(ns cljs-hello.core)

(enable-console-print!)

(def Comment
  (js/React.createClass
   #js {:render (fn []
                  (this-as this
                           (let [comment (.. this -props -comment)]
                             (js/React.DOM.div
                              nil
                              (js/React.DOM.h2 nil  (:author comment))
                              (js/React.DOM.span nil (:text comment))))))}))

(def CommentList 
  (js/React.createClass
   #js {:render (fn []
                  (this-as this
                           (js/React.DOM.div
                            nil
                            (into-array
                             (map #(Comment #js {:comment %})
                                  (.. this -props -comments))))))}))

(def NewComment
  (js/React.createClass
   #js {:render
        (fn []
          (this-as this
                   (let [props (.-props this)]
                     (js/React.DOM.form
                      #js {:onSubmit (.-handleSubmit this)}
                      (js/React.DOM.input
                       #js {:type "text"
                            :ref "author"})
                      (js/React.DOM.input
                       #js {:type "text"
                            :ref "text"})
                      (js/React.DOM.button
                       nil
                       "Submit")))))
        :handleSubmit
        (fn [e]
          (this-as this
                   (let [props (.-props this)
                         author-dom (.getDOMNode (.-author (.-refs this)))
                         text-dom (.getDOMNode (.-text (.-refs this)))]
                     (.newComment props {:author (.-value author-dom) :text (.-value text-dom)}))))}))
        

        
(def CommentBox
  (js/React.createClass
   #js {:getInitialState 
        (fn []
          (this-as this
                   #js {:comments (.-comments (.-props this))}))
        :render (fn []
                  (this-as this
                           (js/React.DOM.div
                            nil
                            (js/React.DOM.h1 nil "Comments")
                            (CommentList #js {:comments (.-comments (.-state this))
                                              :newComment (.-newComment this)})
                            (js/React.DOM.h2 nil "New Comment")
                            (NewComment #js {:newComment (.-newComment this)}))))
        :newComment 
        (fn [c]
          (this-as this
                   (.setState this #js {:comments (conj (.-comments (.-props this)) c)})))}))

(js/React.renderComponent
 (CommentBox #js {:comments [{:author "Mike Sperber" :text "Mikes Kommentar."}
                             {:author "David Frese" :text "Davids Kommentar."}]})
 (.getElementById js/document "content")) 
