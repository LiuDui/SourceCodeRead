## 命令行工具

#### java命令
从那个类启动应用程序，java虚拟机规范没有明确规定。由jvm实现自行决定。Oracle的java虚拟机通过java命令启动，主类名由命令行参数指定。
> - java [-options] class [args]
- java [-options] -jar jarfile [args]
- javaw [-options] class [args]
- javaw [-options] -jar jarfile [args]

可向java命令传递三组参数：选项、主类名（或者JAR文件名）和main方法参数。
> - options：通常由-开头，分为两类：
      - 标准选项：比较稳定，不会轻易变动
      - 非标准选项：以-X开头，很可能会在未来的版本中话，非标准选项中有一部分是高级选项，以-XX开头。
- 第一个非选项参数一般为主类的完全限定名

java与javaw命令基本相同，区别在于javaw不显示命令行窗口，所以适合用于启动GUI应用程序
