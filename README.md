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

每个栈帧内部都包含一个**局部变量表（Local Variable）**，其长度是在**编译期**决定的。一个局部变量可以保存一个类型为`boolean`、`byte`、`char`、`short`、`int`、`float`、`reference`（对象引用）或者`returnAddress`（调用返回地址）的数据，两个局部变量可以保存一个类型为`long`或`double`的数据。这是因为后两种变量类型是64位，前面几种不满32位（比如`byte`、`char`、`short`）会按照一定规则（可能是符号右移也可能是逻辑右移）来补满32位。

局部变量表使用的是索引来进行定位访问，每个位置可以看成一个**Slot**，对**Slot**可以这么理解：

* Java虚拟机会为局部变量表中的每个**Slot**都分配一个访问索引，通过索引可以成功访问到局部变量表中指定的局部变量值
* 如果当前栈帧是由构造方法或者实例方法创建的，那么该对象引用“this”关键字，会存放在索引为0的**Slot**处，其余的局部变量顺序继续排列
* 栈帧的局部变量表的槽位是可以重复的，如果一个局部变量超过了其作用域，那么其作用域之后声明的新的局部变量就有可能会复用过期局部变量的槽位，从而达到节省资源的目的。

Java虚拟机使用局部变量表来完成**方法调用时的参数传递**。当调用类方法时，它的参数将会依次传递到局部变量表从0开始的连续位置上，实例方法则与上面**Slot**理解的第二点相同。

> 类方法：`static`修饰的方法，是属于类而不是属于对象的
>
> 实例方法：创建出的对象所拥有的的方法。



#### 1.2.2 操作数栈

每个栈帧内部都包含一个称为**操作数栈（Operand Stack）**的后进先出栈。操作数栈的最大深度由**编译期**决定。

栈帧刚刚创建的时候，操作数栈是空的。Java虚拟机提供一些**字节码指令**来从局部变量表或者对象实例的字段中赋值常量或者变量值到操作数栈中。也提供了一些指令用于从操作数栈取走数据、操作数据以及把操作结果重新入栈。在调用方法时，操作数栈也用来准备调用方法的参数以及接收方法返回结果。

> 这些指令的名称一般与`load`、`store`有关，`load`是从局部变量表加载到操作数栈中，`store`则是从操作数栈保存到局部变量表，Java虚拟机的运算操作实际上是在操作数栈上运行的。
>
> 举例：`iadd`字节码指令是将两个`int`型的数值相加，它要求执行该指令之前操作数栈顶已经存在两个由前面的其他指令所放入的`int`类型数值，在执行该指令时，操作数栈栈顶的两个`int`型元素出栈（pop），相加求和之后将结果重新入栈（push），要注意的是，Java是一种基于栈运行的语言，而**栈**是一种后进先出的结构，所以对二元运算符来说，先出栈的应该是第二个运算数`val2`，后出栈的是第一个运算数`val1`。这在`iadd`指令可能差别不大，但在减法和除法运算中就有一定的差别。

与局部变量表相同，32位数据或不满32位的数据占用的都是一个栈深度，而64位数据占用的是两个栈深度，在执行字节码指令的时候，大部分指令对操作数栈的栈顶元素都有着类型要求，如果数据类型不同则会抛出异常。那么虚拟机是如何确定这些数据的类型和操作的正确性呢？这其实涉及到对`class`文件的校验过程。



#### 1.2.3 方法调用

- 静态链接：当一个字节码文件被装载进 JVM 内部时，如果被调用的目标方法在编译期可知，且运行时期间保持不变，这种情况下降调用方的符号引用转为直接引用的过程称为静态链接。
- 动态链接：如果被调用的方法无法再编译期被确定下来，只能在运行期将调用的方法的符号引用转为直接引用，这种引用转换过程具备动态性，因此被称为动态链接。
- 方法绑定
	- 早期绑定：被调用的目标方法如果再编译期可知，且运行期保持不变。
	- 晚期绑定：被调用的方法在编译期无法被确定，只能够在程序运行期根据实际的类型绑定相关的方法。
- 非虚方法：如果方法在编译期就确定了具体的调用版本，则这个版本在运行时是不可变的。这样的方法称为非虚方法静态方法，私有方法，final 方法，实例构造器，父类方法都是非虚方法,除了这些以外都是虚方法。
- 虚方法表：面向对象的编程中，会很频繁的使用动态分配，如果每次动态分配的过程都要重新在类的方法元数据中搜索合适的目标的话，就可能影响到执行效率，因此为了提高性能，JVM 采用在类的方法区建立一个虚方法表，使用索引表来代替查找。
	- 每个类都有一个虚方法表，表中存放着各个方法的实际入口。
	- 虚方法表会在类加载的链接阶段被创建，并开始初始化，类的变量初始值准备完成之后，JVM 会把该类的方法也初始化完毕。
- 方法重写的本质
	- 找到操作数栈顶的第一个元素所执行的对象的实际类型，记做 C。如果在类型 C 中找到与常量池中描述符和简单名称都相符的方法，则进行访问权限校验。
	- 如果通过则返回这个方法的直接引用，查找过程结束；如果不通过，则返回 java.lang.IllegalAccessError 异常。
	- 否则，按照继承关系从下往上依次对 C 的各个父类进行上一步的搜索和验证过程。
	- 如果始终没有找到合适的方法，则抛出 java.lang.AbstractMethodError 异常。

Java 中任何一个普通方法都具备虚函数的特征（运行期确认，具备晚期绑定的特点），C++ 中则使用关键字 virtual 来显式定义。如果在 Java 程序中，不希望某个方法拥有虚函数的特征，则可以使用关键字 final 来标记这个方法。



### 1.3 Java堆

Java虚拟机中，**堆（Heap）**是可供各个线程共享的运行时内存区域，也是供所有类实例和数组对象分配内存的区域。**几乎**所有的对象都存储在堆中。

Java堆是线程共享的，所有线程共享一个堆，在Java虚拟机启动的时候堆就会被创建，同时，它存储了被自动内存管理系统所管理的各种对象（垃圾收集器来进行管理）。在Java中，这些对象是无需也无法显式的销毁的。

Java堆中最重要也是最难的就是垃圾回收机制的究竟需要何时触发以及是如何判断一个对象需要回收的，这涉及到一些算法的演化与迭代。



### 1.4 方法区

Java虚拟机中，**方法区（Method Area）**是可供各个线程共享的运行时内存区域。方法区的作用在于存储**每一个类的结构信息**。例如：**运行时常量池（Runtime Constant Pool）**、字段和方法数据、构造函数和普通方法的字节码内容，以及一些其他内容。在Java虚拟机规范中，方法区是对的逻辑组成部分，但可以选择不在这个区域实现垃圾收集与压缩。方法区中的信息可以如下总结：

- 已经被虚拟机加载的类信息
- 常量
- 静态变量
- 即时编译器编译后的代码

与Java堆相同，方法区也在虚拟机启动的时候就被创建。Java虚拟机规范中规定，方法区的容量可以是固定的，也可以是随着程序执行的需求动态扩展的。



#### 1.4.1 运行时常量池

运行时常量池是`class`文件中每一个类或接口的**常量池表**的运行时表示形式。其中包含了若干种不同的常量。当类被 Java 虚拟机加载后， .class 文件中的常量就存放在方法区的运行时常量池中。而且在运行期间，可以向常量池中添加新的常量。如 String 类的 `intern()` 方法就能在运行期间向常量池中添加字符串常量。

> 例子：`("a" + "b" + "c").intern() == "abc"`



### 1.5 本地方法栈

本地方法栈是为 JVM 运行 Native 方法准备的空间，由于很多 Native 方法都是用 C 语言实现的，所以它通常又叫 C 栈。它与 Java 虚拟机栈实现的功能类似，只不过本地方法栈是描述本地方法运行过程的内存模型。

本地方法被执行时，在本地方法栈也会创建一块栈帧，用于存放该方法的局部变量表、操作数栈、动态链接、方法出口信息等。

方法执行结束后，相应的栈帧也会出栈，并释放内存空间。也会抛出 StackOverFlowError 和 OutOfMemoryError 异常。

> 如果 Java 虚拟机本身不支持 Native 方法，或是本身不依赖于传统栈，那么可以不提供本地方法栈。如果支持本地方法栈，那么这个栈一般会在线程创建的时候按线程分配。

***



## 2 ClassFile

每个`class`文件对应的`ClassFile`结构如下所示：

* `Magic Number` -- 魔数
* `minor_version` -- 次版本号
* `major_version` -- 主版本号
* `constant_pool_count` -- 常量池计数器
* `constant_pool[]` -- 常量池
* `access_flags` -- 访问标志
* `this_class` -- 类索引
* `super_class` -- 父类索引
* `interfaces_count` -- 接口计数器
* `interfaces[]` -- 接口表
* `fields_count` -- 字段计数器
* `fields[]` -- 字段表
* `methods_count` -- 方法计数器
* `methods[]` -- 方法表
* `attributes_count` -- 属性计数器
* `attributes[]` -- 属性表

Class 文件是二进制文件，它的内容具有严格的规范，文件中没有任何空格，全都是连续的 0/1。Class 文件 中的所有内容被分为两种类型：无符号数、表。

- 无符号数 无符号数表示 Class 文件中的值，这些值没有任何类型，但有不同的长度。u1、u2、u4、u8 分别代表 1/2/4/8 字节的无符号数。
- 表 由多个无符号数或者其他表作为数据项构成的复合数据类型。



### 2.1 Magic

**魔数**的唯一作用就是确定这个文件是不是一个能够被虚拟机接受的`.class`文件。魔数的固定值是16进制表示下的`0xCAFEBABE`。即在类加载的时候，如果`.class`文件的开始四个byte不是魔数，那么就代表该`.class`文件不是一个规范的class文件，不能够被虚拟机所接受



### 2.2 minor_version/major_version

这两个无符号数代表class文件的**副、主版本号**。我们假设`major_version = M`，`minor_version = m`，则这个class文件的格式版本号就确定为`M.m`。对某个JDK来说，它所能支持的版本号处于一个范围之间，如果class文件的格式版本号不在JDK所支持的版本号之间的话，虚拟机无法运行该class文件



### 2.3 constant_pool

常量池是一种表结构，它包含了class文件结构及其子结构中引用的**所有字符串常量、类或接口名、字段名和其他常量**。其大小由之前的一个2个byte的`constant_pool_count`约束，即常量池的索引是从0 - `constant_pool_count - 1`为范围的。在这之中，一般第一个字节是类型标记，用来确定该项的格式（这在后面的所有表结构中都是一样的），我们将这个字节叫做`tag byte`，简称`tag`



### 2.4 access_flags

access_flags是用来表示某个类或者接口的访问权限以及属性，其具有以下几种标志和含义：

|     标志名     |   值   |                        含义                         |
| :------------: | :----: | :-------------------------------------------------: |
|   ACC_PUBLIC   | 0x0001 |            声明为public，可以从包外访问             |
|   ACC_FINAL    | 0X0010 |              声明为final，不允许有子类              |
|   ACC_SUPER    | 0X0020 | 当用到invokespecial指令时，需要对父类方法做特殊处理 |
| ACC_INTERFACE  | 0X0200 |           该class文件定义的是接口而不是类           |
|  ACC_ABSTRACT  | 0X0400 |             声明为抽象类，不能被实例化              |
| ACC_SYNTHETIC  | 0X1000 |        表示该class文件并非由Java源代码所生成        |
| ACC_ANNOTATION | 0X2000 |                    标识注解类型                     |
|    ACC_ENUM    | 0x4000 |                    标识枚举类型                     |

> Java虚拟机规范中说明：“特殊处理”是相对JDK 1.0.2之前的class文件而言的，invokespecial的语义、处理方式在JDK 1.0.2时发生了改变，为避免二义性，在 JDK 1.0.2 之后编译出的class文件都带有ACC_SUPER 标志用以区分

对于这些访问标志，有一些注意事项：

* 设置了`ACC_INTERFACE`标志的class文件也要同时设置`ACC_ABSTRACT`标志。同时不能设置`ACC_FIANL`，`ACC_SUPER`，`ACC_ENUM`标志
* 如果没有设置`ACC_INTERFACE`标志，那么这个class文件可以具有除了注解类型之外的其他所有标志，`ACC_FIANL`和`ACC_ABSTRACT`这种互斥的不能同时存在。
* Java SE 8及后续版本，无论class文件的标志实际值是什么，Java虚拟机都认为每个class文件设置了`ACC_SUPER`标志。
* 如果设置了`ACC_ANNOTATION`标志，那么也必须设置`ACC_INTERFACE`标志

其他没有涉及到的值是为了未来扩充而预留的，这些预留标志在编译器中设置为0，Java虚拟机实现也应该忽略其它的值



### 2.5 this_class

`this_class`的值必须是对常量池表中某项的一个有效索引值。常量池在这个索引处的成员必须为`Constant_Class_info`类型结构体，该结构体表示这个class文件所定义的类或接口



### 2.6 super_class





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





## Reference

1. Java虚拟机规范 第8版
2. 深入理解Java虚拟机：JVM高级特性与最佳实践
3. [JVM底层原理最全知识总结](https://doocs.github.io/jvm/)
3. 
