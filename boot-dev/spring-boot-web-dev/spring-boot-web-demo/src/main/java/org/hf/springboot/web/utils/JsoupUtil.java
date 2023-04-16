package org.hf.springboot.web.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <p> Jsoup富文本过滤工具类 </p >
 * @author hufei
 * @date 2023-04-11
 **/
public class JsoupUtil {

    /**
     * jsoup白名单种类，有四种，每一种针对的标签类型不一样，具体的可以ctrl+左键点击simpleText，在jsoup源码中有响应的注释和标签名单
     */
    private static final Safelist WHITE_LIST = Safelist.simpleText();
    /**
     * add myself white list label
     */
    private static final Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings().prettyPrint(false);

    static {
        // 将自定义标签添加进白名单，除开白名单之外的标签都会被过滤
        /*whitelist.addAttributes(":all", "style").addTags("Request").addTags("head").addTags("system_code")
                .addTags("waybill_no").addTags("order_code").addTags("body").addTags("route").addTags("Remark")
                .addTags("accept_time").addTags("accept_address").addTags("opcode").addTags("reason_code")
                .addTags("cust_order_code").addTags("status").addTags("info").addTags("push_time");*/
        // 这个配置的意思是,过滤如果找不到成对的标签，就只过滤单个标签，而不用把后面所有的文本都进行过滤。（之前在这个问题上折腾了很久，当<script>标签只有一个时，会<script>标签后面的数据全部过滤）
        WHITE_LIST.preserveRelativeLinks(true);
    }

    // 过滤方法
    public static String clean(String content) {
        return Jsoup.clean(content, "", WHITE_LIST, OUTPUT_SETTINGS);
    }

    // test main
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String text = "<a href=\"http://www.baidu.com/a\" onclick=\"alert(1);\"><style/><strong><p>sss</p ></strong></ a><script>alert(0);</script>sss";
        System.out.println(clean(text));
    }
}