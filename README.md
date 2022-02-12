# jvvm

[toc]

> 南京大学软件学院2019级软件工程与计算一大作业 jvvm



## ClassFile

### Magic Number

### Minor Version

### Major Version

### Constant Pool Count

### Constant Pool

### Access Flags



## ClassLoader

ClassLoader（类加载器）的职责是在运行时将`Java Class`动态的加载到`JVM`中。ClassLoader同时也是`JRE`的一部分，因为有ClassLoader的存在，JVM无需了解底层文件或者文件系统即可运行Java程序。

同时我们知道，Java是一种动态语言，即`Java Class`并不会一次性的全部加载到内存之中，而是在程序需要的时候动态的加载到内存中，这对Java的多态有着极大的好处，即可以在程序中实现**动态绑定**。同时，这种实现就需要ClassLoader来发挥作用。

### Parent Delegation Model

