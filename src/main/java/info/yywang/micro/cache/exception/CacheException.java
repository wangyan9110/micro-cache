package info.yywang.micro.cache.exception;

/**
 * @author yanyan.wang
 * @date 2016-06-08 20:43
 */
public class CacheException extends RuntimeException {

    /**
     * 构造函数
     */
    public CacheException() {
        super();
    }

    /**
     * 构造函数
     * @param message 信息
     */
    public CacheException(String message) {
        super(message);
    }
    /**
     * 构造函数
     * @param message 信息
     * @param cause cause
     */
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
    /**
     * 构造函数
     * @param cause cause
     */
    public CacheException(Throwable cause) {
        super(cause);
    }
}
