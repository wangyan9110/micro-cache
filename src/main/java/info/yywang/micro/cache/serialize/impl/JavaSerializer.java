package info.yywang.micro.cache.serialize.impl;

import info.yywang.micro.cache.exception.CacheException;
import info.yywang.micro.cache.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author yanyan.wang
 * @date 2016-06-08 20:42
 */
public class JavaSerializer implements Serializer {

    @Override
    public Object deserialize(byte[] bytes) {
        Object result = null;
        if (isEmpty(bytes)) {
            return null;
        }
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
            try {
                result = objectInputStream.readObject();
            } catch (ClassNotFoundException ex) {
                throw new CacheException("Failed to deserialize object type", ex);
            }
        } catch (Throwable ex) {
            throw new CacheException("Failed to deserialize", ex);
        }
        return result;
    }

    @Override
    public byte[] serialize(Object object) {
        byte[] result = null;
        if (object == null) {
            return new byte[0];
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(128);
        try {
            if (!(object instanceof Serializable)) {
                throw new CacheException(JavaSerializer.class.getSimpleName() + " requires a Serializable payload " +
                        "but received an object of type [" + object.getClass().getName() + "]");
            }
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            result = byteStream.toByteArray();
        } catch (Throwable ex) {
            throw new CacheException("Failed to serialize", ex);
        }
        return result;
    }

    private static boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }
}
