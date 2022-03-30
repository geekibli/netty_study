# mac系统安装protobuf

## 首先去官网下载对应语言的安装包

[**protobuf 官网下载地址⏬**](https://github.com/protocolbuffers/protobuf/releases) 

官网提供了很多版本的安装包，针对各种语言的，也有all语言支持的安装包，根据自己的需要选择吧。

这里我是下载的 protobuf-all-3.19.4.tar.gz

<img src="https://oscimg.oschina.net/oscnet/up-8a1a74d29f34c2ab7d461dad338da8eda7c.png" width=600 height=480>  

## 下载完成之后放到指定的路径下

这里我是放在了 `/usr/local` 路径下

```
gaolei@gaolei protobuf % pwd
/usr/local/protobuf
```


## 设置编译目录

```
./configure --prefix=/usr/local/protobuf
```

## 切换到root路径下，执行编译

```
sudo -i

make // 如果是下载的 protobuf-all-3.19.4.tar.gz 这个编译过程可能会有点慢
make install
```


## 编译完成之后，就需要配置环境变量了

```
vim .bash_profile   // 我这里用的是 .zshrc  根据自己的shell来


添加protobuf的环境变量

export PROTOBUF=/usr/local/protobuf 
export PATH=$PROTOBUF/bin:$PATH

保存之后，执行source,使配置文件生效

source .bash_profile 
```


## 测试是否安装成功
```
gaolei@gaolei protobuf % protoc --version
libprotoc 3.19.4
```

