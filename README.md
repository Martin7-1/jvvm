# jvvm

[toc]

> 南京大学软件学院2019级软件工程与计算一大作业 jvvm



## 1 ClassFile

### Magic Number

### Minor Version

### Major Version

### Constant Pool Count

### Constant Pool

### Access Flags



## 2 ClassLoader

ClassLoader（类加载器）的职责是在运行时将`Java Class`动态的加载到`JVM`中。ClassLoader同时也是`JRE`的一部分，因为有ClassLoader的存在，JVM无需了解底层文件或者文件系统即可运行Java程序。

同时我们知道，Java是一种动态语言，即`Java Class`并不会一次性的全部加载到内存之中，而是在程序需要的时候动态的加载到内存中，这对Java的多态有着极大的好处，即可以在程序中实现**动态绑定**。同时，这种实现就需要ClassLoader来发挥作用。



### 2.1 Type of ClassLoader

我们可以用下面的一个例子来说明Java中ClassLoader的类型

```java
public void printClassLoaders() throws ClassNotFoundException {
    System.out.println("ClassLoader of this class: " 
                      + PrintClassLoader.class.getClassLoader());
    System.out.println("ClassLoader of Logging:"
                      + Logging.class.getClassLoader());
    System.out.println("ClassLoader of ArrayList:" 
                      + ArrayList.class.getClassLoader());
}
```

> 其中，`PrintClassLoader`是我们自己写的类，`Logging`是第三方类库（<JAVA_HOME>/lib/ext目录下），`ArrayList`是Java自带的库。

运行后我们会得到如下的输出：

```java
Class loader of this class:sun.misc.Launcher$AppClassLoader@18b4aac2
Class loader of Logging:sun.misc.Launcher$ExtClassLoader@3caeaf62
Class loader of ArrayList:null
```



我们可以知道，这里有三种ClassLoader，分别是：

1. Application Classloader or System Classloader
2. Extension Classloader or Platform Classloader
3. Bootstrap Classloader

这三张ClassLoader各有自己负责的领域，其中：

1. Application Classloader负责加载classpath下我们自己的class文件。这个classpath是我们自定义的classpath，即（User Classpath）
2. Extension Classloader负责加载标准Java库以外的扩展库，即<JAVA_HOME>/lib/ext下的第三方库，又或者说是扩展类路径（Extension Classpath）
3. Bootstrap Classloader是另外两类的“父类”（注意这里的父类和Java语言中的某个类 extends 父类不同）。负责加载启动类路径（Bootstrap Classpath）

> 为什么ArrayList的ClassLoader这里会输出`null`呢，这是因为Bootstrap Classloader是用其他语言写的（根据虚拟机的不同可能由不同语言来实现，HotSpot用`Cpp`实现），所以它不会表示为Java Class，而其他的两种ClassLoader则使用Java语言来写的。



#### 2.1.1 Bootstrap Class Loader

我们知道，Java Class都是由`java.lang.ClassLoader`这个类来负责加载的，那么问题是：`java.lang.ClassLoader`这个类是由谁来加载的呢？

这时候就是Bootstap Class Loader来发挥作用的时候，该类加载器主要负责的是JDK内部的类，一般是`rt.jar`和在 $JAVA\_HOME/jre/lib$ 目录下的类，同时，该类加载器也是其他类加载器的“**父类**”



#### 2.1.2 Extension Class Loader

Extension Class Loader是Bootstrap Class Loader的子类（非`Java`中的继承），



### Parent Delegation Model

