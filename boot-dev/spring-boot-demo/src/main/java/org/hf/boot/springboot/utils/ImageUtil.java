package org.hf.boot.springboot.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.constants.FileTypeCollectionEnum;
import org.hf.boot.springboot.constants.FileTypeEnum;
import org.hf.boot.springboot.error.BusinessException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 图片工具处理类
 */
@Slf4j
public class ImageUtil {

    /**
     * 将图片转换成base64
     * @param imgUrl 图片地址
     */
    public static String ImageToBase64(String imgUrl) throws Exception {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        // 下载图片
        URL url = new URL(imgUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(60000);
        conn.connect();
        InputStream is = conn.getInputStream();
        // 将内容读取内存中
        byte[] bytes = new byte[1024];
        int len = -1;
        while ((len = is.read(bytes)) != -1) {
            data.write(bytes, 0, len);
        }
        // 关闭流
        is.close();
        // 对字节数组Base64编码
        return Base64.encodeBase64String(data.toByteArray());
    }

    /**
     * 图像切片（指定切片的宽度和高度）:将一张图片切割成指定高宽的多张小图片
     * 将文件转换成Image对象:ImageIO.read(file)
     * @param srcImage   源图像
     * @param customWidth  目标切片宽度。默认200
     * @param customHeight 目标切片高度。默认150
     * @return 返回所有切片文件
     */
    public static List<File> imageSlice(Image srcImage, int customWidth, int customHeight) {
        // 默认切片宽度
        if (customWidth <= 0) {
            customWidth = 200;
        }
        // 默认切片高度
        if (customHeight <= 0) {
            customHeight = 150;
        }
        // 源图宽度
        int srcWidth = srcImage.getWidth(null);
        // 源图高度
        int srcHeight = srcImage.getHeight(null);
        if (srcWidth < customWidth) {
            customWidth = srcWidth;
        }
        if (srcHeight < customHeight) {
            customHeight = srcHeight;
        }
        // 切片横向数量
        int cols;
        // 切片纵向数量
        int rows;
        // 计算切片的横向和纵向数量
        if (srcWidth % customWidth == 0) {
            cols = srcWidth / customWidth;
        } else {
            cols = (int) Math.floor((double) srcWidth / customWidth) + 1;
        }
        if (srcHeight % customHeight == 0) {
            rows = srcHeight / customHeight;
        } else {
            rows = (int) Math.floor((double) srcHeight / customHeight) + 1;
        }
        // 循环建立切片
        Image tag;
        List<File> fileList = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 四个参数分别为图像起点坐标和宽高
                // 即: CropImageFilter(int x,int y,int width,int height)
                tag = ImgUtil.cut(srcImage,
                        new Rectangle(j * customWidth, i * customHeight, customWidth, customHeight));
                // 输出为文件
                File file = new File("_r" + i + "_c" + j + ".jpg");
                ImgUtil.write(tag, file);
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * 压缩上传的图片
     * @param multiFile 需要压缩的对象
     */
    public static MultipartFile compress(MultipartFile multiFile) throws IOException {
        // 判断文件名称后缀是否在指定的图片类型中
        if (Arrays.stream(FileTypeCollectionEnum.PIC.getFileSuffixArray()).noneMatch(fileType ->
                fileType.endsWith(StringUtils.substringAfter(multiFile.getOriginalFilename(), ".")))) {
            throw new BusinessException("只支持图片格式");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Thumbnails.of(multiFile.getInputStream())
                .scale(1f)
                .outputQuality(0.4f)
                .outputFormat(FileTypeEnum.JPEG.getExt())
                .toOutputStream(bos);
        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
        // 返回压缩图片
        return new MockMultipartFile(DateUtil.format(new Date(), "yyyyMMddHHmmss"),
                multiFile.getOriginalFilename(), FileTypeEnum.JPEG.getContextType(), inputStream);
    }

}
