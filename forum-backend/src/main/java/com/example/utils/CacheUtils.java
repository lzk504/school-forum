package com.example.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class CacheUtils {

    @Resource
    private StringRedisTemplate template;


    /**
     * 将数据保存到缓存中
     *
     * @param key    缓存的键
     * @param data   要保存的数据
     * @param expire 缓存的有效期，单位为秒
     * @param <T>    数据类型
     */
    public <T> void saveCache(String key, T data, long expire) {
        template.opsForValue().set(key, JSONObject.toJSONString(data), expire, TimeUnit.SECONDS);
    }

    /**
     * 将列表数据保存到缓存中
     *
     * @param key    缓存的键
     * @param list   要保存的列表数据
     * @param expire 缓存的有效期，单位为秒
     * @param <T>    列表元素的类型
     */
    public <T> void saveCacheList(String key, List<T> list, long expire) {
        template.opsForValue().set(key, JSONArray.toJSONString(list), expire, TimeUnit.SECONDS);
    }

    /**
     * 从缓存中获取指定键对应的数据，并将其转换为指定类型的对象
     *
     * @param key      缓存的键
     * @param dataType 要转换成的数据类型
     * @param <T>      要转换成的对象类型
     * @return 从缓存中获取的数据转换成的对象，如果缓存中不存在该键，则返回null
     */
    public <T> T takeFromCache(String key, Class<T> dataType) {
        String s = template.opsForValue().get(key);
        if (s == null)
            return null;
        return JSONObject.parseObject(s).to(dataType);
    }

    /**
     * 从缓存中获取指定键对应的列表数据，并将其转换为指定类型的对象列表
     *
     * @param key      缓存的键
     * @param itemType 列表中每个元素的数据类型
     * @param <T>      列表中元素的数据类型
     * @return 从缓存中获取的数据转换成的对象列表，如果缓存中不存在该键，则返回null
     */
    public <T> List<T> takeFromCacheList(String key, Class<T> itemType) {
        String s = template.opsForValue().get(key);
        if (s == null)
            return null;
        return JSONArray.parseArray(s).toList(itemType);
    }


    /**
     * 删除与指定键相关的缓存模式。
     *
     * @param key 指定键
     * @throws IllegalArgumentException 如果 key 为 null
     */
    public void deleteCachePattern(String key) {
        Set<String> keys = Optional.ofNullable(template.keys(key)).orElse(Collections.emptySet());
        template.delete(keys);
    }

    /**
     * 删除指定键的缓存数据
     *
     * @param key 要删除的缓存键
     */
    public void deleteCache(String key) {
        template.delete(key);
    }
}
