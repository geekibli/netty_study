# SpringBoot集成protobuf实战



## 1. 首先先看一下我的项目框架截图，很简单 

<img src="https://oscimg.oschina.net/oscnet/up-1cdb8d5096008e9767aea83d6d1eb08554b.png" width=550 height=300>


## 2. 先创建一个.proto的文件

```
// 使用的proto的版本
syntax = "proto3";

// 定义包名称，如果别的.proto文件需要使用的话，导入包的时候可以使用
package domain;

// 定义proto生成的java类的包名
option java_package = "com.ibli.im.test.domain";

// 定义导出的java类名
option java_outer_classname = "CustomerProtos";

message Customers {
	// repeated 表示list
	repeated Customer customer = 1;
}

message Customer {
	int32 id = 1;
	string firstName = 2;
	string lastName = 3;

	enum EmailType {
		PRIVATE = 0;
		PROFESSIONAL = 1;
	}

	message EmailAddress {
		string email = 1;
		EmailType type = 2;
	}

	repeated EmailAddress email = 5;
}

```


## 3. 使用protobuf反向生成java文件

在项目目录下执行代码： 

```java
gaolei@gaolei IM-test % pwd
/Users/gaolei/Documents/GitHub/IM/IM-test
gaolei@gaolei IM-test % protoc --java_out=./src/main/java ./src/main/resources/Customer.proto
```


## 4. 测试

反向生成java代码之后，我们可以在domain下看到生成的类

接下来我们测试一下：


### 4.1 先向项目中添加所需要的依赖

这个依赖的版本呢和你本地装的protobuf要保持一致，如果版本不一致的话，生成的代码中可能会报错，编译都不成功，更别提运行了。

```xml
	<dependencies>
		<dependency>
			<groupId>com.google.protobuf</groupId>
			<artifactId>protobuf-java</artifactId>
			<version>3.19.4</version>
<!--            <version>4.0.0-rc-2</version>-->
		</dependency>
	</dependencies>
```

可以去这个网站 [maven search](https://search.maven.org/artifact/com.google.protobuf/protobuf-java/4.0.0-rc-2/bundle) 去查找你所需要的依赖。


### 4.2 测试类

```
public class Test {

	public static void main(String[] args) throws InvalidProtocolBufferException {

		CustomerProtos.Customer.EmailAddress.Builder emailBuilder = CustomerProtos.Customer.EmailAddress.newBuilder();
		CustomerProtos.Customer.EmailAddress.Builder privateEmail = emailBuilder.setEmail("1261628527@qq.com").setType(CustomerProtos.Customer.EmailType.PRIVATE);

		CustomerProtos.Customer.EmailAddress.Builder emailBuilder2 = CustomerProtos.Customer.EmailAddress.newBuilder();
		CustomerProtos.Customer.EmailAddress.Builder privateEmail2 = emailBuilder2.setEmail("u-bx@qq.com").setType(CustomerProtos.Customer.EmailType.PROFESSIONAL);

		CustomerProtos.Customer.Builder builder = CustomerProtos.Customer.newBuilder();
		CustomerProtos.Customer gaolei = builder.setFirstName("gao").setLastName("lei")
				.addEmail(privateEmail).addEmail(privateEmail2).build();

		System.out.println(gaolei.toByteArray().length);

		CustomerProtos.Customer customer1 = CustomerProtos.Customer.parseFrom(gaolei.toByteArray());
		System.out.println("result : " + customer1.toString());

		System.out.println(customer1.getEmail(0).getTypeValue());
	}
}
```


### 4.3 我们查看一下测试结果: 

```
48
result : firstName: "gao"
lastName: "lei"
email {
  email: "1261628527@qq.com"
}
email {
  email: "u-bx@qq.com"
  type: PROFESSIONAL
}

0
```






