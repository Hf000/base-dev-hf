package org.hf.application.custom.rpc.core.util;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian序列化工具类
 */
public class HessianSerializer implements MySerializer {

    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(os);
        try {
            ho.writeObject(obj);
            ho.flush();
            return os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeOutputStream(os, ho);
        }
    }

    /**
     * 关闭流
     */
    private void closeOutputStream(ByteArrayOutputStream os, HessianOutput ho) {
        try {
            ho.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        HessianInput hi = new HessianInput(is);
        try {
//            return (T) hi.readObject(clazz);
            return castObject(hi.readObject(clazz), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeInputStream(is, hi);
        }
    }

    /**
     * 关闭流
     */
    private void closeInputStream(ByteArrayInputStream is, HessianInput hi) {
        try {
            hi.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            is.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将Object转换成指定类型
     * @param object 需要转换的对象
     * @param clazz 指定类型
     * @param <T> 返回类型声明
     * @return 返回声明类型对象
     */
    public static <T> T castObject(Object object, Class<T> clazz) {
        // 判断该对象能不能转换成这个类型
        if (clazz.isInstance(object)) {
            // 将该对象转换成指定的类型
            return clazz.cast(object);
        }
        throw new RuntimeException("can not cast " + object.getClass().getName() + " to " + clazz.getName());
    }
}