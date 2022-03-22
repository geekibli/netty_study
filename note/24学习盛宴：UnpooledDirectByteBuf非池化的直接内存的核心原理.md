# 24 学习盛宴：UnpooledDirectByteBuf非池化的直接内存的核心原理

首先代码演示

```java
 public static void main(String[] args) {
        System.setProperty("io.netty.Unsafe", "true");
        ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(9, 100);
        System.out.println(byteBuf);
    }
```
这个底层其实是java NIO的DirectByteBuffer ，这个应该前面也有介绍过。
![](https://oscimg.oschina.net/oscnet/up-dd6be429c23712cbcdc91e9800f0d16993e.png)  

我们直接进入到`UnpooledByteBufAllocator.DEFAULT.directBuffer` 方法中：   
```java
public ByteBuf directBuffer(int initialCapacity, int maxCapacity) {
        if (initialCapacity == 0 && maxCapacity == 0) {
            return this.emptyBuf;
        } else {
            validate(initialCapacity, maxCapacity);
            return this.newDirectBuffer(initialCapacity, maxCapacity);
        }
    }
```

注意的是，上面这个方法也是一个**模版方法**， newDirectBuffer具体的实现在子类中。

![](https://oscimg.oschina.net/oscnet/up-426961232eea433845dbf4a967fc8b2a719.png)

子类如上面所示，我们直接进去到unpooled的子类实现中：  

```java
protected ByteBuf newDirectBuffer(int initialCapacity, int maxCapacity) {
        Object buf;
        if (PlatformDependent.hasUnsafe()) {
            buf = this.noCleaner ? new UnpooledByteBufAllocator.InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf(this, initialCapacity, maxCapacity) : new UnpooledByteBufAllocator.InstrumentedUnpooledUnsafeDirectByteBuf(this, initialCapacity, maxCapacity);
        } else {
            buf = new UnpooledByteBufAllocator.InstrumentedUnpooledDirectByteBuf(this, initialCapacity, maxCapacity);
        }

        return (ByteBuf)(this.disableLeakDetector ? buf : toLeakAwareBuffer((ByteBuf)buf));
    }
```
这里按照是否支持unsafe，分成了两种场景。   
![](https://oscimg.oschina.net/oscnet/up-b05b5ea0a60b745b9a02070ebe7197adb91.png)  

## PlatformDependent 和 PlatformDependent0

主要针对操作系统，JDK版本等环境因素进行判断，判断是否支持堆外内存unsafa以及一些相关联的类， 通过封装unsafe申请堆外内存，释放，获取数据等操作。  


## 不支持unsafe的场景

- 手动设置 `System.setProperty("java.jvm.name", "Dalvik")`  安卓的jvm场景
- 或者手动配置 `System.setProperty("io.Netty.noUnsafe", "true")` 配置环境变量

## io.netty.buffer.UnpooledDirectByteBuf#UnpooledDirectByteBuf(io.netty.buffer.ByteBufAllocator, int, int)
```java
public UnpooledDirectByteBuf(ByteBufAllocator alloc, int initialCapacity, int maxCapacity) {
        super(maxCapacity);
        if (alloc == null) {
            throw new NullPointerException("alloc");
        } else if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity: " + initialCapacity);
        } else if (maxCapacity < 0) {
            throw new IllegalArgumentException("maxCapacity: " + maxCapacity);
        } else if (initialCapacity > maxCapacity) {
            throw new IllegalArgumentException(String.format("initialCapacity(%d) > maxCapacity(%d)", initialCapacity, maxCapacity));
        } else {
            this.alloc = alloc;
            this.setByteBuffer(this.allocateDirect(initialCapacity));
        }
    }
```
这种一般使用与安卓系统中。

这种场景下，Netty底层使用的ByteBuffer其实就是使用的JAVA NIO 的DirectByteBuffer。  

## InstrumentedUnpooledDirectByteBuf 的继承关系

![](https://oscimg.oschina.net/oscnet/up-a5be315e544cc002bfa673fe426f199545e.png)

![](https://oscimg.oschina.net/oscnet/up-a3b248d4f98c71e318bc5df692f9e1b3620.png)

![](https://oscimg.oschina.net/oscnet/up-068ba6702d877e0df113f50962af3d3277a.png)

```java
public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        this.checkSrcIndex(index, length, srcIndex, src.length);
        ByteBuffer tmpBuf = this.internalNioBuffer();
        tmpBuf.clear().position(index).limit(index + length);
        tmpBuf.put(src, srcIndex, length);
        return this;
    }
```
- 首先拿到一个内部的NIo buffer
- tmpBuf 调整到合适的索引，tmp就是用来读写数据的
- 然后讲src中的数据放到tmpBuf中

然后看一下 `internalNioBuffer`方法  
```java
private ByteBuffer internalNioBuffer() {
        ByteBuffer tmpNioBuf = this.tmpNioBuf;
        if (tmpNioBuf == null) {
            this.tmpNioBuf = tmpNioBuf = this.buffer.duplicate(); // 浅层复制 只是复制索引位置，并没有复制底层数据
        }

        return tmpNioBuf;
    }
```
## UnpooledDirectByteBuf的writeIndex 是由谁来负责修改的

> writeIndex的修改在父类writeBytes模版方法中完成


## 下面是UnpooledDirectByteBuf的读数据底层方法
```java
private void getBytes(int index, byte[] dst, int dstIndex, int length, boolean internal) {
        this.checkDstIndex(index, length, dstIndex, dst.length);
        ByteBuffer tmpBuf;
        if (internal) {
            tmpBuf = this.internalNioBuffer();
        } else {
            tmpBuf = this.buffer.duplicate();
        }

        tmpBuf.clear().position(index).limit(index + length);
        tmpBuf.get(dst, dstIndex, length);
    }
```

## 上面是不支持unsafa的情况，相对比较简单，下面是支持unsafe的情况

```java
protected ByteBuf newDirectBuffer(int initialCapacity, int maxCapacity) {
        Object buf;
        if (PlatformDependent.hasUnsafe()) {
            buf = this.noCleaner ? new UnpooledByteBufAllocator.InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf(this, initialCapacity, maxCapacity) : new UnpooledByteBufAllocator.InstrumentedUnpooledUnsafeDirectByteBuf(this, initialCapacity, maxCapacity);
        } else {
            buf = new UnpooledByteBufAllocator.InstrumentedUnpooledDirectByteBuf(this, initialCapacity, maxCapacity);
        }

        return (ByteBuf)(this.disableLeakDetector ? buf : toLeakAwareBuffer((ByteBuf)buf));
    }
```

还是看上面这个代码。 `PlatformDependent.hasUnsafe()` 下，根据是否有cleaner有细分2中不同的情况。


## 什么是 noCleaner的应用场景

- 首先是支持unsafe
- 能够调用unsafe.freeMemory(address) 方法来释放内存
- 能够反射调用 `private DirectByteBuffer(long addr , int cap)` 来分配空间


## 什么是 hasCleaner的应用场景

- 首先是支持unsafe
- 不能反射调用 `private DirectByteBuffer(long addr , int cap)` 来分配空间
- 能够反射调用 ` DirectByteBuffer(int cap)` 来分配空间
- 能够使用 `DirectByteBuffer`的 cleaner的clean方法回收内存


## 以上两种方式有哪些不同

- 分类内存调用构造器不同
- 释放内存调用的方法不同，一个是调用unsafe的方法，一个是调用cleaner的方法

## hasCleaner的构造器

```java
   // Primary constructor
    //
    DirectByteBuffer(int cap) {                   // package-private

        super(-1, 0, cap, cap);
        boolean pa = VM.isDirectMemoryPageAligned();
        int ps = Bits.pageSize();
        long size = Math.max(1L, (long)cap + (pa ? ps : 0));
        Bits.reserveMemory(size, cap);

        long base = 0;
        try {
            base = unsafe.allocateMemory(size);
        } catch (OutOfMemoryError x) {
            Bits.unreserveMemory(size, cap);
            throw x;
        }
        unsafe.setMemory(base, size, (byte) 0);
        if (pa && (base % ps != 0)) {
            // Round up to page boundary
            address = base + ps - (base & (ps - 1));
        } else {
            address = base;
        }
        cleaner = Cleaner.create(this, new Deallocator(base, size, cap)); // 注意这里设置了cleaner对象的值
        att = null;
    }
```

## 什么是cleaner

![](https://oscimg.oschina.net/oscnet/up-214f88696b85ad9906b6c00934fe92402d0.png)

## cleaner的性能问题

![](https://oscimg.oschina.net/oscnet/up-37b9465405ba8b743dc917fa5d1dd8a31d7.png)


## noCleaner的ByteBuf的构造器
```java

    // Invoked to construct a direct ByteBuffer referring to the block of
    // memory. A given arbitrary object may also be attached to the buffer.
    //
    DirectByteBuffer(long addr, int cap, Object ob) {
        super(-1, 0, cap, cap);
        address = addr;
        cleaner = null;
        att = ob;
    }

```

这个的话就非常简单了


```java
protected ByteBuffer allocateDirect(int initialCapacity) {
        return PlatformDependent.allocateDirectNoCleaner(initialCapacity);
    }
```

```javapublic static ByteBuffer allocateDirectNoCleaner(int capacity) {
        assert USE_DIRECT_BUFFER_NO_CLEANER;

        incrementMemoryCounter(capacity);

        try {
            return PlatformDependent0.allocateDirectNoCleaner(capacity);
        } catch (Throwable var2) {
            decrementMemoryCounter(capacity);
            throwException(var2);
            return null;
        }
    }
```


```java
static ByteBuffer allocateDirectNoCleaner(int capacity) {
        return newDirectBuffer(UNSAFE.allocateMemory((long)Math.max(1, capacity)), capacity);
    }
```

## UNSAFE.allocateMemory((long)Math.max(1, capacity)返回直接内存的地址

```java
static ByteBuffer newDirectBuffer(long address, int capacity) {
        ObjectUtil.checkPositiveOrZero(capacity, "capacity");

        try {
			// 真正完成了内存的创建
            return (ByteBuffer)DIRECT_BUFFER_CONSTRUCTOR.newInstance(address, capacity);
        } catch (Throwable var4) {
            if (var4 instanceof Error) {
                throw (Error)var4;
            } else {
                throw new Error(var4);
            }
        }
    }
```


![](https://oscimg.oschina.net/oscnet/up-1f9c414ddcd20d612e30cf5b15d69286304.png)



## noClean的setBytes的模版方法实现

![](https://oscimg.oschina.net/oscnet/up-b00f2786167b812fae1e324bde278cf2d30.png)



## hasCleaner的内存分配

![](https://oscimg.oschina.net/oscnet/up-ffc714ba081923aeed6e4cdf81fe18a96de.png)

![](https://oscimg.oschina.net/oscnet/up-869cc35fc5d4bee607a306f2b39cc352a3c.png)

![](https://oscimg.oschina.net/oscnet/up-fec9f7d6a52e2c5bc3e8619e1f6587464d9.png)

![](https://oscimg.oschina.net/oscnet/up-fd466f507da0439c6cc9c032a1adf8a3528.png)
