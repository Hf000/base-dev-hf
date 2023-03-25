package org.hf.application.custom.shop.proxy;

/**
 * <p> 文件上传接口 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:02
 */
public interface FileUpload {

    /**
     * 文件上传
     * @param buffers 文件二进制
     * @param extName 文件后缀名称
     * @return 上传后的文件名称
     */
    String upload(byte[] buffers, String extName);
}
