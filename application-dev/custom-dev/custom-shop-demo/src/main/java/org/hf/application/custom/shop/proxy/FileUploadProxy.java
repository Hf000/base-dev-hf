package org.hf.application.custom.shop.proxy;

import cn.hutool.core.util.StrUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p> 文件上传代理对象 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:02
 */
@Component
@ConfigurationProperties(prefix = "upload")
public class FileUploadProxy implements ApplicationContextAware {

    /**
     * 注入application.yml中配置实例对应处理的文件类型
     */
    private Map<String, List<String>> fileMap;

    /**
     * 注入Spring的容器对象ApplicationContext
     */
    private ApplicationContext act;

    /**
     * 上传方法
     * @param file 接收文件对象：MultipartFile
     * @return 上传后的文件名称
     * @throws Exception 异常
     */
    public String upload(MultipartFile file) throws Exception {
        //buffers：文件字节数组
        byte[] buffers = file.getBytes();
        //extName：后缀名  1.jpg->jpg
        String fileName = file.getOriginalFilename();
        String extName = StringUtils.getFilenameExtension(fileName);
        if (StrUtil.isBlank(extName)) {
            throw new RuntimeException("文件异常");
        }
        //循环filemap映射关系对象
        for (Map.Entry<String, List<String>> entry : fileMap.entrySet()) {
            //获取指定的value  mp4,avi  |  png,jpg
            List<String> suffixList = entry.getValue();
            //匹配当前用户上传的文件扩展名是否匹配
            for (String suffix : suffixList) {
                if (extName.equalsIgnoreCase(suffix)) {
                    //获取指定key   aliyunOSSFileUpload | fastdfsFileUpload; 一旦匹配执行文件上传
                    String key = entry.getKey();
                    return act.getBean(key, FileUpload.class).upload(buffers, extName);
                }
            }
        }
        return "";
    }

    /**
     * 注入映射配置
     * @param fileMap 入参
     */
    public void setFileMap(Map<String, List<String>> fileMap) {
        this.fileMap = fileMap;
    }

    /**
     * 注入容器
     * @param applicationContext 容器对象
     * @throws BeansException 异常
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        act = applicationContext;
    }
}
