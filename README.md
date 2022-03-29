# Netty

[**官网地址**](https://netty.io/)

## 什么是Netty?

Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers & clients.

> Netty是一个基于时间驱动的异步网络应用框架，应用于开发可维护的，高性能协议的服务端和客户端。

<img src="https://oscimg.oschina.net/oscnet/up-ed74a2022b9f89d51bcf32c18aa755120c9.png" width=550 height=280>


Netty is a NIO client server framework which enables quick and easy development of network applications such as protocol servers and clients. It greatly simplifies and streamlines network programming such as TCP and UDP socket server.

>  Netty是一个NIO客户机-服务器框架，它支持快速、轻松地开发网络应用程序，如协议服务器和客户机。它极大地简化了网络编程，如TCP和UDP套接字服务器。

'Quick and easy' doesn't mean that a resulting application will suffer from a maintainability or a performance issue. Netty has been designed carefully with the experiences earned from the implementation of a lot of protocols such as FTP, SMTP, HTTP, and various binary and text-based legacy protocols. As a result, Netty has succeeded to find a way to achieve ease of development, performance, stability, and flexibility without a compromise.

> “快速简便”并不意味着最终的应用程序会受到可维护性或性能问题的影响。Netty经过精心设计，积累了许多协议（如FTP、SMTP、HTTP以及各种二进制和基于文本的遗留协议）的实施经验。 因此，Netty成功地找到了一种不妥协地实现易开发性、性能、稳定性和灵活性的方法。



## Features

这部分应该都是可以看懂的，就不翻译了。比较基础的专业单词。

### Design
- Unified API for various transport types - blocking and non-blocking socket
- Based on a flexible and extensible event model which allows clear separation of concerns
- Highly customizable thread model - single thread, one or more thread pools such as SEDA
- True connectionless datagram socket support (since 3.1)

### Ease of use
- Well-documented Javadoc, user guide and examples
- No additional dependencies, JDK 5 (Netty 3.x) or 6 (Netty 4.x) is enough
	- Note: Some components such as HTTP/2 might have more requirements. Please refer to the Requirements page for more information.

### Performance
- Better throughput, lower latency
- Less resource consumption
- Minimized unnecessary memory copy

### Security
- Complete SSL/TLS and StartTLS support

### Community
- Release early, release often
- The author has been writing similar frameworks since 2003 and he still finds your feed back precious!


## 使用领域

- 构建高性能、低时延的各种 Java 中间件，例如 **RPC**、**MQ**、分布式服务框架、ESB 消息总线等，Netty 主要作为基础通信框架提供高性能、低时延的通信服务；
- 公有或者私有协议栈的基础通信框架，例如可以基于 Netty 构建异步、高性能的 WebSocket 协议栈；
- 各领域应用，例如大数据、游戏等，Netty 作为高性能的通信框架用于内部各模块的数据分发、传输和汇总等，实现模块之间高性能通信。


## 前置知识

- Java多线程编程，这部分必须要掌握的，否则难以理解Netty。（异步，线程池，线程通信，阻塞队列等）
- 计算机网络（起码熟悉TCP通信）
- 操作系统（了解操作系统基本组件，内存管理，进程调度）
- JVM, 了解运行时数据区，垃圾收集概念
- Java IO, BIO/NIO,能够看懂基本的API使用



## Netty学习由浅到深

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


## 其他资料

- https://dongzl.github.io/netty-handbook/#/README
- https://waylau.com/essential-netty-in-action/index.html






