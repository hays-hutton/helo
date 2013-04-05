(ns helo.service.st.internal)

(defn stringify [any]
  (if (keyword? any)
    (name any)
    (str any)))

(declare kv-to-sv)
(declare scan-kv-to-sv)

(defn each-kv-to-sv "If element is a collection type, do deep transformation"
  [each]
  (if (map? each)
    (kv-to-sv each)
    (if (or (vector? each) (list? each) (seq? each) (set? each))
      (scan-kv-to-sv each)
      each)))

(defn scan-kv-to-sv
  "Scans a collection and turns any contained map within from kv to sv"
  [coll]
  (map each-kv-to-sv coll))

(defn kv-to-sv
  "Transforms keyword-value map {:a 10 :b 20 :c 30}
   to string-value map {\"a\" 10 \"b\" 20 \"c\" 30}"
  [mp]
  (let [m (into {} mp)
        k (keys m)
        v (vals m)]
    (zipmap
      (map stringify k)
      (scan-kv-to-sv v))))
