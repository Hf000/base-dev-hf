package org.hf.application.custom.framework.framework.parse;

import java.io.InputStream;

/**
 * <p> 解析yaml文件实现 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:25
 */
public class ParseYaml extends ParseFile {

    @Override
    public void load(InputStream is) {
        System.out.println("加载yaml文件");
    }
}
