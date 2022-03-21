# 22 学习盛宴：UnpooledHeapByteBuf 非池化的堆内存的核心原理


今天学习一下非池化的堆内存。


## 首先看一下 UnpooledHeapByteBuf 的继承关系

![](https://oscimg.oschina.net/oscnet/up-93182511c22ee85480dcfda953ae303a4f1.png)

## UnpooledHeapByteBuf的重要属性
![](https://oscimg.oschina.net/oscnet/up-24ada5c421eac3425ced6137308cf5a4628.png)


前面已经提到过，ByteBuf这部分使用到了模版模式，有点类似于AQS。

![](https://oscimg.oschina.net/oscnet/up-0805c2a7f5091ceefc97f8050d3a8340729.png)

这是定义的抽象方法，具体的setBytes由具体的子类来实现，不同功能的子类实现自己的逻辑。

![](https://oscimg.oschina.net/oscnet/up-37b3fd8cef9c5b46a3b136d439beb721cb2.png)    

getByte方法逻辑差不多  
![](https://oscimg.oschina.net/oscnet/up-1b525dcbe031d8978baa940710e2f92c2e5.png)


  
## UnpooledHeapByteBuf内存是如何分配的
>  不建议使用构造器分配，而是使用分配器进行分配。
>

![](https://oscimg.oschina.net/oscnet/up-dc79e6f56bc0588b132586f5b081f89a566.png)

![](https://oscimg.oschina.net/oscnet/up-3641d6fbf2893eeb97000a5f49fc446b968.png)

newHeapBuffer也是一个模版方法，不同子类有自己的实现。

```java
 protected ByteBuf newHeapBuffer(int initialCapacity, int maxCapacity) {
        return (ByteBuf)(PlatformDependent.hasUnsafe() ? new UnpooledByteBufAllocator.InstrumentedUnpooledUnsafeHeapByteBuf(this, initialCapacity, maxCapacity) : new UnpooledByteBufAllocator.InstrumentedUnpooledHeapByteBuf(this, initialCapacity, maxCapacity));
    }
```
![](https://oscimg.oschina.net/oscnet/up-53414345fa29cd8f98260d0b28a9de5d625.png)

![](https://oscimg.oschina.net/oscnet/up-987758a0d2e74dec4b040fc817e6a0fc7f5.png)

new byte[] 方式创建数组，如果不主动释放，则由GC程序进行回收。

![](https://oscimg.oschina.net/oscnet/up-81a21626e47599ea95a19e77f2ccd623107.png)

![](https://oscimg.oschina.net/oscnet/up-71830e33cb74587a87e3a8d6bb6a71518bb.png)


## 内存分配钩子方法allocateArray的实现
![](https://oscimg.oschina.net/oscnet/up-838649c65cf1130e87f80794c593982dce1.png)

![](https://oscimg.oschina.net/oscnet/up-c61a467a45d110823fef6556f77cc52bb88.png)

![](https://oscimg.oschina.net/oscnet/up-d17cc30e360b3f338b1c8f020e53377cf0b.png)

![](https://oscimg.oschina.net/oscnet/up-1928ed3effaaf79867026a4fe2bbf15b151.png)

![](https://oscimg.oschina.net/oscnet/up-6c6b5408bf51056eec6e7b52d8754224c11.png)

![](https://oscimg.oschina.net/oscnet/up-998f91af741c9e4347c23bbca6304e0b2fc.png)

![](https://oscimg.oschina.net/oscnet/up-6e1f8d38eda187c5d86824422ea18583953.png)

![](https://oscimg.oschina.net/oscnet/up-7da050121cda8240279d6fa4498402ad99a.png)

