package org.hf.application.custom.framework.framework.parse;

import java.io.InputStream;

/**
 * <p> 解析文件的抽象 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:24
 */
public abstract class ParseFile {

    /**
     * 加载指定文件
     * @param is 需要解析的文件流对象
     * @throws Exception 异常
     */
    public abstract void load(InputStream is) throws Exception;
}
