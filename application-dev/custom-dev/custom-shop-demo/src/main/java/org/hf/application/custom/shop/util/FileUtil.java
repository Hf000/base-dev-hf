package org.hf.application.custom.shop.util;

/**
 * <p> 文件处理工具类 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:04
 */
public class FileUtil {

    /**
     * 获取文件类型
     * @param fileNameExtension 文件后缀名称
     * @return 文件类型
     */
    public static String getContentType(String fileNameExtension) {
        if (".bmp".equalsIgnoreCase(fileNameExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileNameExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileNameExtension) ||
                ".jpg".equalsIgnoreCase(fileNameExtension) ||
                ".png".equalsIgnoreCase(fileNameExtension)) {
            return "image/jpg";
        }
        if (".html".equalsIgnoreCase(fileNameExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileNameExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileNameExtension)) {
            return "application/vnd.visio";
        }
        if (".pptx".equalsIgnoreCase(fileNameExtension) ||
                ".ppt".equalsIgnoreCase(fileNameExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".docx".equalsIgnoreCase(fileNameExtension) ||
                ".doc".equalsIgnoreCase(fileNameExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileNameExtension)) {
            return "text/xml";
        }
        if (".mp4".equalsIgnoreCase(fileNameExtension)) {
            return "video/mp4";
        }
        if (".avi".equalsIgnoreCase(fileNameExtension)) {
            return "video/x-msvideo";
        }
        return "image/jpg";
    }
}
