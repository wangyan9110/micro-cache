package info.yywang.micro.cache.redis;

import info.yywang.micro.cache.Cache;
import info.yywang.micro.cache.CacheKey;
import info.yywang.micro.cache.Lifecycle;
import info.yywang.micro.cache.serialize.Serializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author yanyan.wang
 * @date 2016-06-10 17:50
 */
public class RedisCache<T extends Serializable> implements Cache<T>, Lifecycle {

    private Serializer serializer;

    private Properties properties;

    private JedisPool jedisPool;

    public RedisCache(Serializer serializer, Properties properties) {
        this.serializer = serializer;
        this.properties = properties;
    }

    @Override
    public void put(CacheKey cacheKey, T obj) {
        put(cacheKey, (Object) obj);
    }

    @Override
    public void put(CacheKey cacheKey, List<T> objs) {
        put(cacheKey, (Object) objs);
    }

    @Override
    public void put(CacheKey cacheKey, Object obj) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(cacheKey.getBytes(), serializer.serialize(obj));
            if (!cacheKey.isDefaultExp()) {
                jedis.expire(cacheKey.getBytes(), cacheKey.getExp() * 60);
            }
        } finally {
            jedis.close();
        }
    }

    @Override
    public void update(CacheKey cacheKey, T obj) {
        put(cacheKey, obj);
    }

    @Override
    public void update(CacheKey cacheKey, List<T> objs) {
        put(cacheKey, objs);
    }

    @Override
    public void update(CacheKey cacheKey, Object obj) {
        put(cacheKey, obj);
    }

    @Override
    public T get(CacheKey cacheKey) {
        return (T) getObj(cacheKey);
    }

    @Override
    public List<T> getList(CacheKey cacheKey) {
        return (List<T>) getObj(cacheKey);
    }

    @Override
    public Object getObj(CacheKey cacheKey) {
        Jedis jedis = jedisPool.getResource();
        try {
            byte[] bytes = jedis.get(cacheKey.getBytes());
            return serializer.deserialize(bytes);
        } finally {
            jedis.close();
        }
    }

    @Override
    public Map<CacheKey, T> get(List<CacheKey> cacheKeys) {
        Map<CacheKey, Object> cacheKeyObjectMap = getObjs(cacheKeys);
        Map<CacheKey, T> cacheKeyTMap = new HashMap<CacheKey,T>();
        for (CacheKey cacheKey : cacheKeys) {
            cacheKeyTMap.put(cacheKey, (T) cacheKeyObjectMap.get(cacheKey));
        }
        return cacheKeyTMap;
    }

    @Override
    public Map<CacheKey, List<T>> getLists(List<CacheKey> cacheKeys) {
        Map<CacheKey, Object> cacheKeyObjectMap = getObjs(cacheKeys);
        Map<CacheKey, List<T>> cacheKeyTMap = new HashMap<CacheKey, List<T>>();
        for (CacheKey cacheKey : cacheKeys) {
            cacheKeyTMap.put(cacheKey, (List<T>) cacheKeyObjectMap.get(cacheKey));
        }
        return cacheKeyTMap;
    }

    @Override
    public Map<CacheKey, Object> getObjs(List<CacheKey> cacheKeys) {
        Jedis jedis = jedisPool.getResource();
        try {
            Map<CacheKey, Object> keyObjectMap = new HashMap<CacheKey, Object>();
            for (CacheKey cacheKey : cacheKeys) {
                byte[] bytes = jedis.get(cacheKey.getBytes());
                keyObjectMap.put(cacheKey, serializer.deserialize(bytes));
            }
            return keyObjectMap;
        } finally {
            jedis.close();
        }
    }

    @Override
    public void remove(CacheKey cacheKey) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del(cacheKey.getBytes());
        } finally {
            jedis.close();
        }
    }

    @Override
    public void remove(List<CacheKey> cacheKeys) {
        Jedis jedis = jedisPool.getResource();
        try {
            for (int index = 0; index < cacheKeys.size(); index++) {
                byte[] key = cacheKeys.get(index).getBytes();
                jedis.del(key);
            }
        } finally {
            jedis.close();
        }
    }

    @Override
    public void start() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        int maxTotal = Integer.parseInt(properties.getProperty("cache.redis.pool.maxTotal", "-1"));
        if (maxTotal != -1) {
            jedisPoolConfig.setMaxIdle(maxTotal);
        }
        int maxIdle = Integer.parseInt(properties.getProperty("cache.redis.pool.maxIdle", "-1"));
        if (maxIdle != -1) {
            jedisPoolConfig.setMaxIdle(maxIdle);
        }
        int minIdle = Integer.parseInt(properties.getProperty("cache.redis.pool.minIdle", "-1"));
        if (minIdle != -1) {
            jedisPoolConfig.setMinIdle(minIdle);
        }
        long maxWaitMillis = Long.parseLong(properties.getProperty("cache.redis.pool.maxWaitMillis", "-1"));
        if (maxWaitMillis != -1) {
            jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        }
        boolean testOnBorrow = Boolean.parseBoolean(properties.getProperty("cache.redis.pool.testOnBorrow", "false"));
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        boolean testOnReturn = Boolean.parseBoolean(properties.getProperty("cache.redis.pool.testOnBorrow", "false"));
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        String host = properties.getProperty("cache.redis.host", "127.0.0.1");
        int port = Integer.parseInt(properties.getProperty("cache.redis.port", "6379"));
        int timeout = Integer.parseInt(properties.getProperty("cache.redis.timeout", "1000"));
        String password = properties.getProperty("cache.redis.password", "");
        if ("".equals(password.trim())) {
            this.jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
        } else {
            this.jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        }
    }

    @Override
    public void shutdown() {
        if (this.jedisPool != null) {
            jedisPool.destroy();
        }
    }
}
