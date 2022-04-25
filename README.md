# Netty入门到实战

> 前置总结

[**什么是Netty**](https://github.com/geekibli/netty/blob/gaolei/note/0.什么是Netty.md)

## Netty打造百万级链接的IM系统

> 下面主要但不限于，根据 [**架构师尼恩**](https://www.cnblogs.com/crazymakercircle/) 的Nerry课程来学习的，特此声明。

- [16.彻底明白内核态、用户态、内核空间和用户空间](https://github.com/geekibli/netty/blob/gaolei/note/16.彻底明白内核态、用户态、内核空间和用户空间.md)
- [17.彻底理解java零拷贝](https://github.com/geekibli/netty/blob/gaolei/note/17.彻底明白java零拷贝.md)
- [18.彻底理解java零拷贝](https://github.com/geekibli/netty/blob/gaolei/note/18彻底理解java零拷贝.md)
- [19.ByteBuf的基本原理，模版模式，引用计数、核心类等](https://github.com/geekibli/netty/blob/gaolei/note/19.ByteBuf的基本原理，模版模式，引用计数、核心类等.md)
- [20.彻底明白ByteBuf的自动创建](https://github.com/geekibli/netty/blob/gaolei/note/20.彻底明白ByteBuf的自动创建.md)
- [21.彻底明白ByteBuf的自动释放](https://github.com/geekibli/netty/blob/gaolei/note/21.彻底明白ByteBuf的自动释放.md)
- [22.UnpooledHeapByteBuf%20非池化的堆内存的核心原理](https://github.com/geekibli/netty/blob/gaolei/note/22学习盛宴：UnpooledHeapByteBuf%20非池化的堆内存的核心原理.md)
- [23.NIO的DirectByteBuffer核心原理](https://github.com/geekibli/netty/blob/gaolei/note/23.学习盛宴：NIO的DirectByteBuffer核心原理.md)
- [24.UnpooledDirectByteBuf非池化的直接内存的核心原理](https://github.com/geekibli/netty/blob/gaolei/note/24学习盛宴：UnpooledDirectByteBuf非池化的直接内存的核心原理.md)
- [25.PooledDirectByteBuf池化的直接内存的核心原理](https://github.com/geekibli/netty/blob/gaolei/note/25.PooledDirectByteBuf池化的直接内存的核心原理.md)
- [26.百万级别聊天室，服务端channel是如何存放的（1）](https://github.com/geekibli/netty/blob/gaolei/note/26.百万级别聊天室，服务端channel是如何存放的（1）.md)
- [27.百万级别聊天室，服务端channel是如何存放的（2）](https://github.com/geekibli/netty/blob/gaolei/note/27.百万级别聊天室，服务端channel是如何存放的（2）.md)
- [28.百万级别聊天室，服务端channel是如何存放的（3）](https://github.com/geekibli/netty/blob/gaolei/note/28.百万级别聊天室，服务端channel是如何存放的（3）.md)
- [29.Netty灵魂实验%20：本地_100W连接_超_高并发实验，瞬间提升Java内力](https://github.com/geekibli/netty/blob/gaolei/note/29.Netty灵魂实验%20：本地_100W连接_超_高并发实验，瞬间提升Java内力.md)
- [30.彻底明白：select、poll底层系统调用的核心原理](https://github.com/geekibli/netty/blob/gaolei/note/30.彻底明白：select、poll%20底层系统调用的核心原理.md)
- [31.彻底明白：epoll底层系统调用的核心原理](https://github.com/geekibli/netty/blob/gaolei/note/31.彻底明白：epoll%20底层系统调用的核心原理.md)
- [32.核心解密：Linux的ET高速模式与Netty的高速Selector](https://github.com/geekibli/netty/blob/gaolei/note/32.核心解密：Linux的ET高速模式与Netty的高速Selector.md)
- [33.彻底解密：IO事件的核心原理](https://github.com/geekibli/netty/blob/gaolei/note/33.彻底解密：IO事件的核心原理.md)
<!-- - [34.彻底解密：SelectionKey (选择键) 核心原理]()
- [35.彻底明白：Selector(选择器) 核心原理]()
- [36.最强揭秘：Selector.open() 选择器打开的底层原理]()
- [37.最强揭秘：Selector.register() 注册的底层原理]()
- [38.最强揭秘：Selector.select() 事件查询的底层原理]()
- [39.最强揭秘：Selector.wakeup() 唤醒的底层原理]() -->
- [34.彻底解密：SelectionKey(选择键)核心原理.md](https://github.com/geekibli/netty/blob/gaolei/note/34.彻底解密：SelectionKey%20(选择键)%20核心原理.md)
- [40.从底揭秘：Thread.sleep是不是占用CPU](https://github.com/geekibli/netty/blob/gaolei/note/40.从底揭秘：Thread.sleep是不是占用CPU.md)
- [41.线程状态的本质和由来](https://github.com/geekibli/netty/blob/gaolei/note/41.线程状态的本质和由来.md)
- [43.Netty开发必备：EmbeddedChannel嵌入式通道](https://github.com/geekibli/netty/blob/gaolei/note/43.Netty开发必备：EmbeddedChannel嵌入式通道.md)
- [44.基础知识：ChannelPipeline流水线](https://github.com/geekibli/netty/blob/gaolei/note/44.基础知识：ChannelPipeline流水线.md)
- [45.基础组件的使用：ChannelInboundHandler入站处理器](https://github.com/geekibli/netty/blob/gaolei/note/45.基础组件的使用：ChannelInboundHandler入站处理器.md)
- [46.基础实战：Pipeline入站处理流程](https://github.com/geekibli/netty/blob/gaolei/note/46.基础实战：Pipeline入站处理流程.md)
- [47.基础实战：Pipeline出站处理流程](https://github.com/geekibli/netty/blob/gaolei/note/47.基础实战：Pipeline出站处理流程.md)
- [48.核心实战：JSON报文的入站处理](https://github.com/geekibli/netty/blob/gaolei/note/48.核心实战：JSON报文的入站处理.md)
- [49.核心实战：JSON报文的出站处理](https://github.com/geekibli/netty/blob/gaolei/note/49.核心实战：JSON报文的出站处理.md)
- [50.基础实战：protobuf协议的使用](https://github.com/geekibli/netty/blob/gaolei/note/50.基础实战：protobuf协议的使用.md)
- [51.基础实战：protobuf入站报文的实现](https://github.com/geekibli/netty/blob/gaolei/note/51.基础实战：protobuf入站报文的实现.md)
- [52.基础实战：protobuf出站报文的处理](https://github.com/geekibli/netty/blob/gaolei/note/52.基础实战：protobuf出站报文的处理.md)
- [53.单体IM结构设计](https://github.com/geekibli/netty/blob/gaolei/note/53.单体IM结构设计.md)
- [54.单体IM基础实战：命令收集器](https://github.com/geekibli/netty/blob/gaolei/note/54.单体IM基础实战：命令收集器.md)
- [55.单体IM基础实战：消息构造器](https://github.com/geekibli/netty/blob/gaolei/note/55.单体IM基础实战：消息构造器.md)
- [56.单体IM基础实战：消息发送器](https://github.com/geekibli/netty/blob/gaolei/note/56.单体IM基础实战：消息发送器.md)
- [57.单体IM基础实战：响应处理器](https://github.com/geekibli/netty/blob/gaolei/note/57.单体IM基础实战：响应处理器.md)
- [58.基础实战：服务端登录处理](https://github.com/geekibli/netty/blob/gaolei/note/58.基础实战：服务端登录处理.md)
- [59.基础实战：业务逻辑异步处理](https://github.com/geekibli/netty/blob/gaolei/note/59.基础实战：业务逻辑异步处理.md)
- [60.单体IM核心编程：服务端会话管理](https://github.com/geekibli/netty/blob/gaolei/note/60.%20单体IM核心编程：服务端会话管理.md)
- [61.单体IM核心编程：单聊-端到端的聊天转发](https://github.com/geekibli/netty/blob/gaolei/note/61单体IM核心编程：单聊-端到端的聊天转发.md)
- [62.单体IM核心编程：端到端的心跳处理](https://github.com/geekibli/netty/blob/gaolei/note/62单体IM核心编程：端到端的心跳处理.md)

## 其他笔记

- [SpringBoot集成protobuf实战](https://github.com/geekibli/netty/blob/gaolei/note/SpringBoot集成protobuf实战.md)
- [mac安装protobuf教程](https://github.com/geekibli/netty/blob/gaolei/note/mac安装protobuf教程.md)
- [NIO实现Reactor单线程模型](https://github.com/geekibli/netty/blob/gaolei/note/NIO实现Reactor单线程模型.md)
- [NIO实现Reactor多线程模型](https://github.com/geekibli/netty/blob/gaolei/note/NIO实现Reactor多线程模型.md)
- [NIO实现主从Reactor多线程模型](https://github.com/geekibli/netty/blob/gaolei/note/NIO实现主从Reactor多线程模型.md)
- [Netty内存池（史上最全 + 5W字长文）](https://www.cnblogs.com/crazymakercircle/p/16181994.html)


## Netty面试题

- [10道Java高级必备的面试题](https://github.com/geekibli/netty/blob/gaolei/interview/10道Java高级必备的面试题.md)
- [10道Netty高级面试题](https://github.com/geekibli/netty/blob/gaolei/interview/10道Netty高级面试题.md)


## 其他链接

- https://dongzl.github.io/netty-handbook/#/README
- https://waylau.com/essential-netty-in-action/index.html






