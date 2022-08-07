package org.hf.application.javabase.apply.jdk8.stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileDemo {

    public static void main(String[] args) throws IOException {

        //获取所有的文件列表信息
        //Files.list(Paths.get("D:\\workspace\\hufei\\demo")).forEach(path-> System.out.println(path));
        Files.list(Paths.get("D:\\workspace\\hufei\\demo")).forEach(path-> {

            System.out.println(path);

            //读取每一个文件中的内容信息
            try {
                Files.lines(path).forEach(content-> System.out.println(content));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }
}
