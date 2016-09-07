package info.yywang.micro.cache;

/**
 * @author yanyan.wang
 * @date 2016-06-10 17:11
 */
public interface Lifecycle {

    /**
     * 启动
     */
    void start();

    /**
     * 关闭
     */
    void shutdown();
}
