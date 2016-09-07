package info.yywang.micro.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yanyan.wang
 * @date 2016-06-08 20:50
 */
public class CacheKey {

    /**
     * 默认过期时间
     */
    public final static int DEFAULT_EXP = -1;

    /**
     * 缓存key
     */
    private String key;

    /**
     * 指定过期时间(单位分钟)
     * 默认永久有效
     */
    private int exp = DEFAULT_EXP;

    /**
     * 参数
     */
    private List<String> params;


    private CacheKey() {

    }

    private CacheKey(String key) {
        this.key = key;
    }

    private static CacheKey create(String key) {
        return create(key, new String[0]);
    }

    private static CacheKey create(String key, int exp) {
        return create(key, exp, new String[0]);
    }

    public static CacheKey create(String key, String... params) {
        return create(key, DEFAULT_EXP, params);
    }

    public static CacheKey create(String key, int exp, String... params) {
        CacheKey cacheKey = new CacheKey(key);
        cacheKey.setExp(exp);
        cacheKey.setParams(Arrays.asList(params));
        return cacheKey;
    }

    public static CacheKey create(String key, int... params) {
        return create(key, DEFAULT_EXP, params);
    }

    public static CacheKey create(String key, int exp, int... params) {
        CacheKey cacheKey = new CacheKey(key);
        cacheKey.setExp(exp);
        cacheKey.params = new ArrayList<String>();
        for (int param : params) {
            cacheKey.params.add(param + "");
        }
        return cacheKey;
    }

    public static <K> CacheKey create(String key, K... params) {
        return create(key, DEFAULT_EXP, params);
    }

    public static <K> CacheKey create(String key, int exp, K... params) {
        CacheKey cacheKey = new CacheKey(key);
        cacheKey.setExp(exp);
        cacheKey.params = new ArrayList<String>();
        for (K param : params) {
            cacheKey.params.add(param.toString());
        }
        return cacheKey;
    }

    public CacheKey add(int param) {
        this.params.add(param + "");
        return this;
    }

    public CacheKey add(String param) {
        this.params.add(param);
        return this;
    }

    public <K> CacheKey add(K param) {
        this.add(param.toString());
        return this;
    }

    public byte[] getBytes() {
        return this.toString().getBytes();
    }

    public boolean isDefaultExp() {
        return this.exp == DEFAULT_EXP;
    }

    @Override
    public String toString() {
        return key + "_" + Arrays.toString(params.toArray(new String[params.size()]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CacheKey cacheKey = (CacheKey) o;
        return this.toString().equals(cacheKey.toString());

    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    public String getKey() {
        return key;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    private void setParams(List<String> params) {
        this.params = params;
    }
}
