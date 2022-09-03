package org.hf.application.javabase.jdk8.stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * <p> stream构建文件流 </p>
 *
 * @author hufei
 * @date 2022/9/3 16:42
 */
public class FileDemo {

    public static void main(String[] args) throws IOException {

        //获取所有的文件列表信息
        //Files.list(Paths.get("D:\\workspace\\hufei\\demo")).forEach(path-> System.out.println(path));
        Files.list(Paths.get("D:\\workspace\\hufei\\demo")).forEach(path -> {
            System.out.println(path);
            //读取每一个文件中的内容信息
            try {
                Files.lines(path).forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
