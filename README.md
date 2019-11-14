记录项目过程
##分布式session
redis 使用fastjson序列化，设置FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(MiaoShaUser.class)，
才不会出现问题，设置Object.class 会出现问题？