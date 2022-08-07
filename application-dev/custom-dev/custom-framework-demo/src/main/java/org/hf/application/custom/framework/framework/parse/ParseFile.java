package org.hf.application.custom.framework.framework.parse;

import java.io.InputStream;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:24
*/
public abstract class ParseFile {

    //加载指定文件
    public abstract void load(InputStream is) throws Exception;
}
