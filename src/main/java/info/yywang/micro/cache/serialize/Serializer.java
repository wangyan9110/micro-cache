package info.yywang.micro.cache.serialize;

/**
 * @author yanyan.wang
 * @date 2016-06-08 20:38
 */
public interface Serializer {

    /**
     * 反序列化
     *
     * @param bytes
     * @return
     */
    Object deserialize(byte[] bytes);

    /**
     * 序列化
     *
     * @param object
     * @return
     */
    byte[] serialize(Object object);
}
