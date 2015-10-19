(ns mbfpp.oo.tinyweb.core
   (:require [clojure.string :as str])
   (:import (com.mblinn.oo.tinyweb RenderingException ControllerException)))

(defn- render [view model]
  (try
     (view model)
     (catch Exception e (throw (RenderingException. e )))))

(defn test-controller [http-request]
  {:name (http-request :body)})

(defn test-view [model]
  (str "<h1>Hello," (model :name) "</h1>")
  )

(def test-request-handler {:controller test-controller
                           :view test-view})

(def test-http-request {:body "Mike" 
                        :path "/say-hello"
                        :headers{}})

(defn- execute-request[http-request handler]
  (let [controller (handler :controller)  
        view (handler :view)]
;如果没有异常抛出，try表达式将以携带有两对键值对的map的值作为返回值，该map代表了我们的HTTP响应，
;map中的第一个键是:status-code,对应的值是200，
;第二个键是:body，它对应的值由以下过程产生：
;    首先controller函数对传入的http-request进行计算，然后controller将计算结果传递给render函数，
;最后由render函数将controller的计算结果与view一起渲染产生出该结果值。
    (try
      {:status-code 200
       :body 
       (render
         view
         (controller http-request))}
      (catch ControllerException e {:status-code (.getStatusCode e) :body ""})
      (catch RenderingException e {:status-code 500
                                   :body "Exception while rendering"})
      (catch Exception e (.printStackTrace e){:status-code 500 :body ""}))))

(execute-request test-http-request test-request-handler)



;函数以一个过滤器序列和一个HTTP请求作为入参，它将这些过滤器混合成一个单独的过滤器
;然后将它应用于该HTTP请求
(defn- apply-filters [filters http-request]
  (let [composed-filter (reduce comp (reverse filters))]
    (composed-filter http-request)))


;该函数以一个请求处理器的map和一个过滤器序列为入参，它同样返回了一个函数，该函数以一个HTTP请求为入参
;同时使用了apply-filters来将所有的过滤器应用于该请求
;接着，该函数从HTTP请求中获取请求路径，然后再请求处理器的map中定位到合适的处理器，并使用execute-request
;来执行该处理器
(defn tinyweb [request-handlers filters]
  (fn [http-request]
    (let [filtered-request (apply-filters filters http-request)
                             path (http-request :path)
                             handler (request-handlers path)]
      (execute-request filtered-request handler))))





