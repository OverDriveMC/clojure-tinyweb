(ns mbfpp.oo.tinyweb.example
  (:use [mbfpp.oo.tinyweb.core])
  (:require [clojure.string :as str]))

(def request {:path "/greeting"
              :body "Mike,Joe,John,Steve"})

;控制器的代码
(defn make-greeting[name]
  (let [greetings ["Hello" "Greetings" "Salutations" "Hola"]
        greeting-count (count greetings)]
    (str (greetings (rand-int greeting-count)) "," name)))

(defn handle-greeting [http-request]
  {:greetings (map make-greeting (str/split (:body http-request) #","))})

;运行测试请求，并返回合适的模型map
(handle-greeting request)

;该代码将模型渲染到HTML，他也是一个函数，以合适的模型map作为入参，并返回一个字符串
(defn render-greeting [greeting]
  (str "<h2>" greeting "</h2>"))

;\newline使用换行符连接
(defn greeting-view [model]
  (let [rendered-greetings (str/join \newline (map render-greeting (:greetings model)) )]
    (str "<h1>Friendly Greetings</h1>" rendered-greetings )))

(greeting-view (handle-greeting request))

;这是一个用于在请求返回前记录其访问路径的简单函数
(defn logging-filter [http-request]
  (println (str "In logging Filter -request for path: " 
                (:path http-request))) http-request)

(def request-handlers
  {"/greeting" {:controller handle-greeting
                :view greeting-view}})

(def filters [logging-filter])

(def tinyweb-instance (tinyweb request-handlers filters))


(tinyweb-instance request)

