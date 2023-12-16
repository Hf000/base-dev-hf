package org.hf.boot.springboot.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件比较
 */
public class FileCompareUtil {

    private FileCompareUtil() {}

    /**
     * 文件比较方法
     * @param sourcePath    源文件路径, 例如:D:\Users\IdeaProjects\workspace\projectDemoMaster
     * @param targetPath    目标文件路径 例如:D:\Users\IdeaProjects\workspace\projectDemoDeveloper
     */
    public static void compareController(String sourcePath, String targetPath) {
        // 源文件
        List<String> sourceList = new ArrayList<>();
        getAllFilesByController(sourcePath, sourceList);
        System.out.println(sourceList.size());
        // 目标文件
        List<String> targetList = new ArrayList<>();
        getAllFilesByController(targetPath, targetList);
        System.out.println(targetList.size());
        // 比较不同路径相关文件的指定内容的区别, 例如这里是比较Controller中api是否有新增
        List<String> addList = new ArrayList<>();
        for (String str : sourceList) {
            if (!targetList.contains(str)) {
                addList.add(str);
                System.out.println(str);
            }
        }
        System.out.println("addUrl----" + addList);
    }

    /**
     * 递归获取指定路径下文件名中包含Controller的文件内容有@PostMapping或者@GetMapping的接口名称
     */
    private static void getAllFilesByController(String path, List<String> urlList) {
        File file = new File(path);
        File[] tempList = file.listFiles();
        if (tempList == null) {
            return;
        }
        for (File dto : tempList) {
            if (dto.isFile()) {
                String fileName = dto.getName();
                if (!fileName.contains("Controller")) {
                    continue;
                }
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(Files.newInputStream(dto.toPath())));
                    String line;
                    String controllerFilename = dto.getName().replace(".java", "");
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("@PostMapping") || line.contains("@GetMapping")) {
                            int firstSignatureIndex = line.indexOf("\"");
                            int lastSignatureIndex = line.lastIndexOf("\"");
                            String apiName = line.substring(firstSignatureIndex + 1, lastSignatureIndex);
                            urlList.add(controllerFilename + "-" + apiName);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                // 递归查找
                getAllFilesByController(dto.getAbsolutePath(), urlList);
            }
        }
    }
}