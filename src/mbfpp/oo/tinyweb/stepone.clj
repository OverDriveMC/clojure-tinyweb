;1:定义命名空间，一个命名空间是指代码库中的函数集合，它可以在其它命名空间中被完全或部分地导入

;2:导入了两个Java类：HttpRequest和HttpRequest$Builder(只不过是我们在HttpRequest中作为其中一部分
;创建的一个静态内部类Builder的全名。clojure并没有为静态内部类提供特殊的语法，所以我们只能使用完整类名)

;3:关键词    :import 则是一个Clojure关键字的例子，一个关键字就是提供了非常快速的相等性检查的标识符
;通常前面会带上一个冒号。
;这里使用了 :import关键字表明哪些类应该被导入到我们刚才声明的命名空间中，但是除此之外，关键字还有很多用途
;例如通常在map中作为键来使用
(ns mbfpp.oo.tinyweb.stepone
  (:import (com.mblinn.oo.tinyweb.HttpRequest HttpRequest$Builder)))
;接受一个来自原Java版本的HttpRequest，然后产出一个Clojure map作为模型
(defn test-controller [http-request]
;调用HttpRequest的getBody()方法获得请求体，并使用它来创建一个具有单对键值对的map
;而map的键的关键字 :name，值是HttpRequest的字符串请求体
  {:name (.getBody http-request)})

(def test-builder(HttpRequest$Builder/newBuilder))
(def test-http-request (.. test-builder (body "Mike") (path "/say-hello") build))

(def test-controller-with-map [http-request]
  {:name (http-request :body)})

;创建了一个HttpRequest$Builder并使用它来创建新的HttpRequest
;这段代码使用了两个新的Clojure与Java的互操作特性，
;    首先，方法前的   斜杠/ 让我们可以调用一个类的静态方法或引用一个静态变量
;因此片段(HttpRequest$Builder/newBuilder是指调用在类HttpRequest$Builder中的方法newBuilder())
(def test-builder(HttpRequest$Builder/newBuilder))
;    另一个特性是     ..宏 ,这是一个相当便捷的互操作特性，可以非常容易的在Java对象上调用一连串方法。它的工作方式是将第一个
;参数传递给 .. ,然后将对后续参数的调用贯串起来。
;首先以"Mike"为入参调用test-builder上的方法body()，获得结果以后，以入参"say-hello"调用该返回结果上的path()方法
;最后在path()返回的结果上调用build()方法，并返回一个HttpRequest实例
(def test-http-request(.. test-builder (body "Mike") (path "/say-hello") build))
