package org.hf.application.custom.shop.controller;

import org.hf.application.custom.shop.proxy.FileUploadProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:56
*/
@RestController
@RequestMapping(value = "/file")
public class FileController {

    @Autowired
    private FileUploadProxy fileUploadProxy;

    /***
     * 文件上传
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/upload")
    public String upload(MultipartFile file) throws Exception {
        return fileUploadProxy.upload(file);
    }
}
