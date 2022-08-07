package org.hf.application.custom.shop.proxy;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:02
*/
public interface FileUpload {

    /****
     * 文件上传
     */
    String upload(byte[] buffers , String extName);
}
