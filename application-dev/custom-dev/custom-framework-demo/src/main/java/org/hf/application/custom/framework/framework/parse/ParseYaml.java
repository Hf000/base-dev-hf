package org.hf.application.custom.framework.framework.parse;

import java.io.InputStream;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:25
*/
public class ParseYaml extends ParseFile {
    @Override
    public void load(InputStream is) throws Exception {
        System.out.println("加载yaml文件");
    }
}
