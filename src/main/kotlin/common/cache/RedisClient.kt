package org.example.common.cache

import kotlinx.serialization.Serializer
import org.example.common.exceptions.CustomException
import org.example.common.exceptions.Errcode
import org.redisson.api.RedissonClient
import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.TimeUnit

class RedisClient(
    private val template: RedisTemplate<String, String>,
    private val redissonClient: RedissonClient
) {
    fun get(key: String): String? {
        return template.opsForValue().get(key);
    }

    fun <T> get(key: String, kSerializer: (Any) -> T?): T? {
        val value = get(key);

        value?.let{ return kSerializer(it)} ?:return null
    }

    fun setIfNotExist(key: String, value: String): Boolean{
        return template.opsForValue().setIfAbsent(key,value) ?: false
    }

    fun <T> invokeWithMutex(key: String, function : () -> T?) {
        val lock = redissonClient.getLock(key);

        try{
            lock.lock(15, TimeUnit.SECONDS);
            function.invoke();
        }catch (e: Exception) {
            throw CustomException(Errcode.FAILED_TO_MUTEX_INVOLE,key)
        }finally {
            lock.unlock();
        }
    }
}