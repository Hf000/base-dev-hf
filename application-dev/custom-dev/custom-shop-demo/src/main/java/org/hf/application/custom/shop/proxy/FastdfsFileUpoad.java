package org.hf.application.custom.shop.proxy;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * <p> 文件上传 -- FastDFS文件上传实现 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:01
 */
@Component(value = "fastdfsFileUpoad")
public class FastdfsFileUpoad implements FileUpload {

    /**
     * fastDFS服务地址
     */
    @Value("${fastdfs.url}")
    private String url;

    // 初始化tracker信息
    static {
        try {
            //获取tracker的配置文件fdfs_client.conf的位置
            String filePath = new ClassPathResource("fdfs_client.conf").getPath();
            //加载tracker配置信息
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     * @param buffers：文件字节数组
     * @param extName：文件后缀名
     * @return 上传后的文件名称
     */
    @Override
    public String upload(byte[] buffers, String extName) {
        /*
         * 文件上传后的返回值
         * uploadResults[0]:文件上传所存储的组名，例如:group1
         * uploadResults[1]:文件存储路径,例如：M00/00/00/wKjThF0DBzaAP23MAAXz2mMp9oM26.jpeg
         */
        String[] uploadResults;
        try {
            //获取StorageClient对象
            StorageClient storageClient = getStorageClient();
            //执行文件上传
            uploadResults = storageClient.upload_file(buffers, extName, null);
            return url + uploadResults[0] + "/" + uploadResults[1];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取StorageClient
     * @return StorageClient
     * @throws Exception 异常
     */
    public static StorageClient getStorageClient() throws Exception {
        //创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient获取TrackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //通过TrackerServer创建StorageClient
        return new StorageClient(trackerServer, null);
    }
}
