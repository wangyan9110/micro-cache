package info.yywang.micro.cache.serialize.impl;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.HessianOutput;
import info.yywang.micro.cache.exception.CacheException;
import info.yywang.micro.cache.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author yanyan.wang
 * @date 2016-06-10 16:14
 */
public class Hessian2Serialzer implements Serializer {

    @Override
    public Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        Hessian2Input hi = new Hessian2Input(is);
        try {
            return hi.readObject();
        } catch (IOException ex) {
            throw new CacheException("Failed to serialize", ex);
        }
    }

    @Override
    public byte[] serialize(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(os);
        try {
            ho.writeObject(object);
            return os.toByteArray();
        } catch (IOException ex) {
            throw new CacheException("Failed to serialize", ex);
        }
    }
}
