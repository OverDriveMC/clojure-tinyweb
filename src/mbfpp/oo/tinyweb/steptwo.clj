(ns mbfpp.oo.tinyweb.steptwo
  (:import (com.mblinn.oo.tinyweb.RenderingException)))
;我们期望test-request是一个以:body为键的map，它代表了HTTP请求的请求体，
;通过该键从map中获取到值之后，又将该值放入了一个代表模型的新map之中
(defn test-controller [http-request]
  {:name (http-request :body)})

;采用<h1>标记来对名字信息进行了包装
(defn test-view [model]
  (str "<h1>Hello, " (model :name) "</h1>"))
;测试模型
(def test-model {:name "Mike"})
(test-view test-model)


(defn- render[view,model]
  (try
    (view model)
    (catch Exception e (throw (RenderingException. e)))
    )
  )
