# mybatis-async
mybatis-async,是封装了mybatis的异步框架。

## 逻辑图如下：
 ![image](https://github.com/LeiXuan6/mybatis-async/raw/master/src/test/java/com/gameart/async/img/mybatis-async.jpg)

### 工程依赖
+ Jdk 1.8
+ Maven 3.x
+ mybits 3.x

### 调用示例
+ 实现规范
   + AsyncMethod 注解, 标记Mapper的某个方法是异步调用的
   + AsyncType   注解, 异步的类型

+ Mapper实现规范的示列

```
@Mapper
public interface UserMapper {

  com.gameart.async.domain.User getUser(String userId);

  int insert(@Param("userId") String userId, @Param("name") String name);

  @AsyncMethod(type = AsyncType.UPDATE)
  void update(@Param("userId") String userId,@Param("name")String name);
}

``` 

+ 异步任务提交示列
```
     UserMapper userMapper = ProxyFactory.factory(UserMapper.class).newInstance();
     userMapper.update(id, "proxy");
```
 + 具体列子参考BasicTest,ProxyTest
