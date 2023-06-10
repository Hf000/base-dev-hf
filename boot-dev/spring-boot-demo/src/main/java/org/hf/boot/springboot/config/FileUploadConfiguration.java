package org.hf.boot.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

/**
 * <p> 文件上传配置 </p>
 * @author HUFEI
 * @date 2023-05-12
 **/
@Slf4j
@Configuration
public class FileUploadConfiguration {
    
    private static final String[] SIZE_UNIT_ARRAY = {"KB", "MB", "GB", "TB", "B"};
    
    /**
     * 文件上传配置
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement(@Value("${file.maxFileSize:10240KB}") String maxFileSize, @Value("${file.maxRequestSize:20240KB}") String maxRequestSize) {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个文件最大
        factory.setMaxFileSize(parseFileDataSize(maxFileSize));
        // 设置总上传数据总大小
        factory.setMaxRequestSize(parseFileDataSize(maxRequestSize));
        return factory.createMultipartConfig();
    }

    /**
     * 解析获取文件大小
     */
    private DataSize parseFileDataSize(String fileSize) {
        if (StringUtils.isBlank(fileSize)) {
            return null;
        }
        try {
            for (String sizeUnit : SIZE_UNIT_ARRAY) {
                fileSize = fileSize.toUpperCase();
                if (fileSize.contains(sizeUnit)) {
                    long size = Long.parseLong(fileSize.replaceAll(sizeUnit, ""));
                    DataUnit dataUnit = DataUnit.fromSuffix(sizeUnit);
                    return DataSize.of(size, dataUnit);
                }
            }
        } catch (Exception e) {
            log.error("文件上传大小限制错误，原因：", e);
        }
        return null;
    }
}
