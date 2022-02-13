# jvvm

[toc]

> 南京大学软件学院2019级软件工程与计算一大作业 jvvm



## 1 JVM内存结构

Java虚拟机的内存空间由以下的五个部分组成：

1. 程序计数器（PC）
2. Java虚拟机栈（Java Virtual Machine Stack）
3. 本地方法栈
4. 堆（Heap）
5. 方法区（Method Area）

其中，程序计数器、本地方法栈和Java虚拟机栈是线程隔离的，即每个线程都拥有这三个东西，其他两个部分则是线程共享的，即所有线程所拥有的都是同一个堆和同一个方法区。线程隔离的数据区域会随着县城开始和结束而创建和销毁



![](https://s2.loli.net/2022/02/13/fN8xqBgYTGsUiFj.png)

> 图源：[JVM内存结构](https://doocs.github.io/jvm/01-jvm-memory-structure.html#%E7%A8%8B%E5%BA%8F%E8%AE%A1%E6%95%B0%E5%99%A8-pc-%E5%AF%84%E5%AD%98%E5%99%A8)



### 1.1 程序计数器

每一个Java虚拟机线程都有属于自己的PC（Program Counter）寄存器。在任意时刻，一个Java虚拟机线程只会执行一个方法的代码，而PC所指向的就是当前Java虚拟机正在执行的字节码指令的地址。如果当前线程所执行的方法是本地方法（native），那么此时程序计数器为`Undefined`。

需要注意的是，在Java虚拟机中很多分支控制的指令都是跳转**包括本身在内的指令条数**，这是因为在取指之后，PC已经指向了下一条指令（即内容已经变成下一条指令的地址），而一般的跳转指令（如`goto`，`if<cond>`等）要求的都是从上一条指令的地址开始计算跳转的字节数。即我们需要根据如下公式来计算指令跳转的位置：
$$
pc = pc - intsrLength + offset
$$
这部分留到字节码指令部分再具体介绍



**程序计数器的作用**

* 字节码解释器通过改变程序计数器来以此读取指令，从而实现代码的流程控制
* 在多线程情况下，程序计数器记录的是当前线程执行的位置，方便线程切换中的指令地址保存。

> 在所有Java虚拟机内存区域中，PC是唯一一个不会出现`OutOfMemoryError`的内存区域



### 1.2 Java虚拟机栈

Java虚拟机栈也是线程私有的一块内存空间，其目的是用于存储栈帧（Stack Frame）。栈帧中会存放一些方法运行过程中的信息，比如：

1. 局部变量表
2. 操作数栈
3. 动态链接
4. 方法出口信息
5. ......

![](https://s2.loli.net/2022/02/13/iybIG8Dwm2NQeWC.png)

> 图源：[JVM内存结构](https://doocs.github.io/jvm/01-jvm-memory-structure.html#java-%E8%99%9A%E6%8B%9F%E6%9C%BA%E6%A0%88%E7%9A%84%E5%AE%9A%E4%B9%89)



方法运行时需要创建局部变量的时候，局部变量的值会存入栈帧的局部变量表中。Java 虚拟机栈的栈顶的栈帧是当前正在执行的活动栈，也就是当前正在执行的方法，PC 寄存器也会指向这个地址。只有这个活动的栈帧的本地变量可以被操作数栈使用，当在这个栈帧中调用另一个方法，与之对应的栈帧又会被创建，新创建的栈帧压入栈顶，变为当前的活动栈帧。

方法结束后，当前栈帧被移出，栈帧的返回值变成新的活动栈帧中操作数栈的一个操作数。如果没有返回值，那么新的活动栈帧中操作数栈的操作数没有变化。

Java虚拟机规范既允许Java虚拟机被实现成固定大小，也允许根据计算来动态扩展和收缩。Java虚拟机栈可能发生如下的异常情况：

1. 如果线程请求分配的栈容量超过Java虚拟机栈允许的最大容量，Java虚拟机将会抛出一个`StackOverflowError`异常
2. 如果Java虚拟机栈可以动态扩展，并且在尝试扩展的时候无法申请到足够的内存，或者在创建新的线程时没有足够的内存去创建对应的虚拟机栈，那Java虚拟机将会抛出一个`OutOfMemoryError`异常



下面介绍一些栈帧中的一些内存空间，每一个栈帧都有自己的局部变量表（Local Variable）、操作数栈（Operand Stack）和指向当前方法所属类的运行时常量池（Run-time Constant Pool）的引用。



#### 1.2.1 局部变量表



#### 1.2.2 操作数栈



#### 1.2.3 动态链接



## 2 ClassFile

### Magic Number

### Minor Version

### Major Version

### Constant Pool Count

### Constant Pool

### Access Flags



## 3 ClassLoader

ClassLoader（类加载器）的职责是在运行时将`Java Class`动态的加载到`JVM`中。ClassLoader同时也是`JRE`的一部分，因为有ClassLoader的存在，JVM无需了解底层文件或者文件系统即可运行Java程序。

同时我们知道，Java是一种动态语言，即`Java Class`并不会一次性的全部加载到内存之中，而是在程序需要的时候动态的加载到内存中，这对Java的多态有着极大的好处，即可以在程序中实现**动态绑定**。同时，这种实现就需要ClassLoader来发挥作用。



### 3.1 Type of ClassLoader

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



#### 3.1.1 Bootstrap Class Loader

我们知道，Java Class都是由`java.lang.ClassLoader`这个类来负责加载的，那么问题是：`java.lang.ClassLoader`这个类是由谁来加载的呢？

这时候就是Bootstap Class Loader来发挥作用的时候，该类加载器主要负责的是JDK内部的类，一般是`rt.jar`和在 <JAVA_HOME>/jre/lib 目录下的类，同时，该类加载器也是其他类加载器的“**父类**”



#### 3.1.2 Extension Class Loader

Extension Class Loader是Bootstrap Class Loader的子类（非`Java`中的继承），主要负责的是对标准Java库之外的扩展类进行类加载，一般加载的是 <JAVA_HOME>/lib/ext目录下的类



#### 3.1.3 Application Class Loader

Application Class Loader主要负责加载的是应用级别的类，即在我们自定义的classpath下来进行类加载，在用`javac`进行编译的时候，我们可以通过`-cp/-classpath`来自行指定classpath。该类加载器是Extension Class Loader的子类



![](https://s2.loli.net/2022/02/12/5hlTNzDrbeun2Lm.png)

> 图源：南京大学软件工程2019级软件工程与计算I大作业手册



### 3.2 Load Class

类加载器在对类进行Java的流程可以大致简述如下：

1. JVM需要加载某个类的时候，通过`java.lang.ClassLoader.loadClass()`方法来通过**某个类的全限定名**在运行时加载该类。
2. 首先检查这个类是否被加载，如果这个类还没有被加载的话，会将任务委派给父类加载器，并不断递归进行
3. 当最顶层的类加载器（此时没有父类，无法继续递归）并没有在自己的classpath中找到该类的话，会向下委派。
4. 收到上层委派的类加载器会通过`java.net.URLClassLoader.findClass()`来在文件系统中查找对应要加载的类
5. 如果到了最下层的类加载器都没有找到所需的类，那么就会抛出`java.lang.ClassNotFoundException`或者`java.lang.NoClassDefFoundError`



如果我们查看某个`ClassNotFoundException`的例子：

```java
java.lang.ClassNotFoundException: com.baeldung.classloader.SampleClassLoader    
    at java.net.URLClassLoader.findClass(URLClassLoader.java:381)    
    at java.lang.ClassLoader.loadClass(ClassLoader.java:424)    
    at java.lang.ClassLoader.loadClass(ClassLoader.java:357)    
    at java.lang.Class.forName0(Native Method)    
    at java.lang.Class.forName(Class.java:348)
```

我们会发现其过程就是按照我们上述所说的步骤来进行的。接下来我们会详细介绍这个过程中很重要的一个机制：双亲委派机制（Parent Delegation Model），也就是上文步骤中两次出现的“**委派**”二字



#### 3.2.1 Parent Delegation Model

> 双亲委派模型

简单来说，双亲委派模型的流程可以用一句话解释：当JVM需要加载某个class时，底层的类加载器会将任务委派给它的父类加载器，只有在父类加载器无法加载该类的时候，底层的类加载器才会来尝试加载该类。

举个例子：当我们有一个加载application class到JVM的请求，首先Application Class Loader会将请求委派给其父类Extension Class Loader，然后Extension Class Loader会向上委派给Bootstrap Class Loader。

此时Bootstrap Class Loader已经没有父加载器了，所以会开始尝试加载需要的类，当它加载失败时会告知Extension Class Loader，然后由Extension Class Loader来尝试加载该类，加载失败后会告知Application Class Loader，最后由Application Class Loader来加载该类。



#### 3.2.2 Visibility

不同类加载器之间的类具有可见性。子类加载器对其父类加载器加载的类是可见的（**children class loaders are visible to classes loaded by their parent class loaders**）。

举个例子，现在我们有一个由Application Class Loader加载的类A，和一个由Extension Class Loader加载的类B，则无论A或者B都是Application Class Loader可见的，但对于Extension Class Loader来说只有类B可见



### 3.3 自定义Classloader

