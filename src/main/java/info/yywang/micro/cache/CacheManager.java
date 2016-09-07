package info.yywang.micro.cache;

import info.yywang.micro.cache.redis.RedisCache;
import info.yywang.micro.cache.serialize.Serializer;
import info.yywang.micro.cache.serialize.impl.Hessian2Serialzer;
import info.yywang.micro.cache.serialize.impl.JavaSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

/**
 * @author yanyan.wang
 * @date 2016-06-10 16:49
 */
public class CacheManager {

    /**
     * the cache
     */
    private static Cache cache;

    /**
     * the lifecycle
     */
    private static Lifecycle lifecycle;

    /**
     * the logger
     */
    private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);

    private static final String PATH = "/data/appdatas/cache/config/cache.properties";

    static {

        try {
            Properties properties = new Properties();
            String path = PATH;
            if (System.getProperty("os.name").toLowerCase().contains("window")) {
                path = "C:" + path;
            }
            properties.load(new FileInputStream(path));
            String serializerType = properties.getProperty("cache.serializer", "java");
            Serializer serializer = null;
            if ("java".equals(serializerType)) {
                serializer = new JavaSerializer();
            } else if ("hessian2".equals(serializerType)) {
                serializer = new Hessian2Serialzer();
            }
            cache = new RedisCache(serializer, properties);
            lifecycle = new RedisCache(serializer, properties);
            lifecycle.start();
            logger.info("Cache init finished!");
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    lifecycle.shutdown();
                    logger.info("Cache shutdown!");
                }
            }));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取Cache对象
     *
     * @param <T>
     * @return
     */
    public static <T extends Serializable> Cache<T> getCache() {
        return cache;
    }

    /**
     * shutdown
     */
    public static void shutdown() {
        lifecycle.shutdown();
    }
}
