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