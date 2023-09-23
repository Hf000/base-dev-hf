package org.hf.application.socket;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端Socket
 * @author hf
 */
public class SocketClient {

    public static void main(String[] args) throws Exception {
        //创建Socket对象，与服务端Socket建立连接, 指定需要连接的主机地址和端口
        Socket socket = new Socket("127.0.0.1", 8080);
        //获取输出流对象
        OutputStream outputStream = socket.getOutputStream();
        System.out.println("客户端阻塞，接收键盘输入....");
        //接收键盘输入，模拟延迟消息发送
        Scanner scanner = new Scanner(System.in);
        String scannerString = scanner.next();
        //使用输出流对象写入数据
        outputStream.write(scannerString.getBytes());
        System.out.println("客户端录入完成....");
        //释放资源
        socket.close();
    }
}