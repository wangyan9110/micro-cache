package info.yywang.micro.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author yanyan.wang
 * @date 2016-06-08 20:49
 */
public interface Cache<T extends Serializable> {

    /**
     * put obj
     *
     * @param cacheKey
     * @param obj
     */
    void put(final CacheKey cacheKey, final T obj);

    /**
     * put objs
     *
     * @param cacheKey
     * @param objs
     */
    void put(final CacheKey cacheKey, final List<T> objs);

    /**
     * put obj
     *
     * @param cacheKey
     * @param obj
     */
    void put(final CacheKey cacheKey, final Object obj);

    /**
     * update by the cacheKey
     *
     * @param cacheKey
     * @param obj
     */
    void update(final CacheKey cacheKey, final T obj);

    /**
     * update by the cacheKey
     *
     * @param cacheKey
     * @param objs
     */
    void update(final CacheKey cacheKey, final List<T> objs);

    /**
     * update by the cacheKey
     *
     * @param cacheKey
     * @param obj
     */
    void update(final CacheKey cacheKey, final Object obj);

    /**
     * get obj by cache key
     *
     * @param cacheKey
     * @return
     */
    T get(final CacheKey cacheKey);

    /**
     * get objs by cache key
     *
     * @param cacheKey
     * @return
     */
    List<T> getList(final CacheKey cacheKey);

    /**
     * get obj by cachekey
     *
     * @param cacheKey
     * @return
     */
    Object getObj(final CacheKey cacheKey);

    /**
     * get by cacheKeys
     *
     * @param cacheKeys
     * @return
     */
    Map<CacheKey, T> get(final List<CacheKey> cacheKeys);

    /**
     * get by cacheKeys
     *
     * @param cacheKeys
     * @return
     */
    Map<CacheKey, List<T>> getLists(final List<CacheKey> cacheKeys);

    /**
     * get the objs
     *
     * @param cacheKeys
     * @return
     */
    Map<CacheKey, Object> getObjs(final List<CacheKey> cacheKeys);

    /**
     * remove the key
     *
     * @param cacheKey
     */
    void remove(final CacheKey cacheKey);

    /**
     * remove the key
     *
     * @param cacheKeys
     */
    void remove(final List<CacheKey> cacheKeys);
}
