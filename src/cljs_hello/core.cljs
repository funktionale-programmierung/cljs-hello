(ns cljs-hello.core)

(enable-console-print!)

(def Comment
  (js/React.createClass
   #js {:render (fn []
                  (this-as this
                           (let [props (.-props this)]
                             (js/React.DOM.div
                              nil
                              (js/React.DOM.h2 nil  (.-author props))
                              (js/React.DOM.span nil (.-text props))))))}))

(def CommentList 
  (js/React.createClass
   #js {:render (fn []
                  (this-as this
                           (js/React.DOM.div
                            nil
                            (into-array
                             (map (fn [c] (Comment #js {:author (:author c) :text (:text c)}))
                                  (.-comments (.-props this)))))))}))

(def NewComment
  (js/React.createClass
   #js {:render
        (fn []
          (this-as this
                   (js/React.DOM.form
                    #js {:onSubmit (.-handleSubmit this)}
                    (js/React.DOM.input
                     #js {:type "text" :ref "author"})
                    (js/React.DOM.input
                     #js {:type "text" :ref "text"})
                    (js/React.DOM.button nil "Submit"))))
        :handleSubmit
        (fn [e]
          (this-as this
                   (let [props (.-props this)
                         refs (.-refs this)
                         author-dom (.getDOMNode (.-author refs))
                         text-dom (.getDOMNode (.-text refs))]
                     (.newComment props {:author (.-value author-dom) :text (.-value text-dom)})))
          false)})) ; this prevents submitting the form to the "server" and reloading everything
        

        
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
                            (CommentList #js {:comments (.-comments (.-state this))})
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
