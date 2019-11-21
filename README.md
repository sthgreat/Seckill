记录项目过程
##分布式session
redis 使用fastjson序列化，设置FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(MiaoShaUser.class)，
才不会出现问题，设置Object.class 会出现问题？

https://blog.csdn.net/songzehao/article/details/99641594 拦截器的使用

使用mybatis进行联表查询（注解）：未开启驼峰映射时，使用results指定映射（不需要考虑表名）
开启驼峰映射后会自动匹配（表名无影响，只看表名后面的字段）

mysql数据插入后，id自动映射到原来的对象的id字段中

压测：qps每秒处理请求

打war包与jar包：mvn clean package

GET POST区别：
GET幂等，从服务端获取数据，不对服务端造成影响
POST向服务端提交数据，对服务端有影响
#------------------------#
页面优化技术：
页面缓存+url缓存+对象缓存
页面静态化+前后端分离

页面缓存：
将页面保存到缓存中（这里选用的是redis服务器，一定时间内访问该页面均返回缓存的页面）
url级缓存：
