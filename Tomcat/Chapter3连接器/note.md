# 连接器

> 连接器的主要作用：接受http请求，创建`javax.servlet.http.HttpServletRequest`和`javax.servlet.http.HttpServletResponse`对象，并将这两个对象传入`service()`方法中。

### `org.apache.catalina.util`包下的`StringManager`类
> 该类用于处理不同模块和Catalina本身中错误消息和国际化操作。

#### 一个包下一个propertise文件存储错误信息

Tomcat处理错误消息的方法是将错误消息存储在一个properties文件中，便于读取和编辑。但是若将所有错误信息放在一个properties文件中，则会导致该文件过于庞大，不利于维护。

Tomcat为解决错误信息放在一个文件中导致文件庞大的问题，使用的解决方案为：**将properties文件划分到不同的包中**，每个包下的properties文件包含该包下任何类可能抛出的所有异常信息。

对于国际化的支持，需要创建properties文件的地方，都会创建多个（三个），命名不同，对应不同的语言。
* LocalStrings.properties - 英文
* LocalStrings_es.properties - 西班牙语
* LocalStrings_jp.properties - 日文

#### 一个`StringManager`实例处理一个properties文件

每个存储错误信息的properties文件需要一个StringManager类的实例来处理，当包中某个类需要在其包内的properties文件中查找错误信息时，会首先获取对应的StringManager类的实例，为了少创建对象，同一个包下的所有类公用一个StringManager对象，单例模式。

在`StringManager`类中维护一个Hashtable对象，key是包名，value是StringManager类的实例，所以可以直接通过包名进行查找。

该StringManger对象会根据语言环境自动读取对应语言的properties文件。

当需要获取错误消息时，可以使用StringManger的`getString`方法的到对应的错误信息。
例如在英文环境下，包`ex03.pymont.connector.http`下的properties中存储内容为：
```
name=hello
```
那么直接使用
```java
StringManger manger = StringManager.getManager("ex03.pymont.connector.http");
String name = manger.getString("name");
```
### 应用程序
应用按模块划分为：连接器模块、启动模块、核心模块：
* 连接器模块
  > * 连接器机器支持类（HttpConnector和HttpProcessor）
  * 表示Http请求类（HttpRequest）及其支持类
  * 表示Http响应类(HttpResponse)及其支持类
  * 外观类（HttpRequestFacade和HttpResponseFacade）
  * 常量类
* 启动模块
  > 只有一个类，Bootstrap，负责应用程序的启动
* 核心模块
  > ServletProcesstor和StaticResourceProcesstor

#### 与第二章的对比
