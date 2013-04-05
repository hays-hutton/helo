(ns helo.service.st
  (:import org.antlr.stringtemplate.StringTemplateGroup)
  (:import org.antlr.stringtemplate.StringTemplate)
  (:use helo.service.st.internal))

(defn create-view "Return new view template - useful as mentioned here:
  http://hardlikesoftware.com/weblog/2006/12/12/using-json-with-stringtemplate/"
  ([]
    (StringTemplate.))
  ([^String template]
    (StringTemplate. template)))

(defn get-view-from-classpath "Return the view template from classpath"
  [^String view-name]
  (let [st-group (StringTemplateGroup. "default")]
    (.getInstanceOf st-group view-name)))

(defn get-view-from-dir "Return the view template from specified directory"
  [^String view-name ^String root-dir]
  (let [st-group (StringTemplateGroup. "default" root-dir)]
    (.getInstanceOf st-group view-name)))

(defn reset-view! "Reset view template with supplied content"
  [^StringTemplate view ^String template]
  (.setTemplate view template))

(defn fill-view! "Fill view template with key/value pairs"
  ;;;
  ;; Fill template with key and value
  ([^StringTemplate template k v]
    (.setAttribute template (stringify k) (each-kv-to-sv v))
    template)
  ;;;
  ;; Fill template with key/value from map
  ([^StringTemplate template kv-map]
    (.setAttributes template (kv-to-sv kv-map))
    template))

(defn render-view "Return rendered view for the template"
  [^StringTemplate template]
  (.toString template))

(defn render "Returns string from preloaded view and fills it in with map values"
  [view props]
  (render-view (fill-view! view props)))

