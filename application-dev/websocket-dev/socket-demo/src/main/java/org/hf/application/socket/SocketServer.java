package org.hf.application.socket;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 定义服务端Socket
 * 此方式的问题: 不能异步处理客户端的连接, 如果有资源连接了且没有释放, 后面的连接就无法处理
 * @author hf
 */
public class SocketServer {

    public static void main(String[] args) throws Exception {
        //创建ServerSocket对象，用于客户端的连接, 绑定指定的端口号
        ServerSocket serverSocket = new ServerSocket(8080);
        //定义输入流对象读取数据
        byte[] bytes = new byte[1024];
        try {
            // noinspection InfiniteLoopStatement
            while (true) {
                System.out.println("服务端发生阻塞，等待连接....");
                //调用accept方法监听客户端，阻塞方法
                Socket accept = serverSocket.accept();
                //调用Socket对象的方法获取输入流对象
                InputStream inputStream = accept.getInputStream();
                System.out.println("服务端发生阻塞，等待接收数据....");
                int readFlag;
                while ((readFlag = inputStream.read(bytes)) != -1) {
                    System.out.println(new String(bytes, 0, readFlag));
                }
                //关闭资源
                accept.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
    }
}