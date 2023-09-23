package org.hf.application.socket;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Nio server  1、阻塞; 2、并发
 * 解决{@link SocketServer} 方式出现的问题, 这里可以进行异步处理连接请求
 * @author hf
 */
public class SocketNioServer {

    public static void main(String[] args) throws Exception {
        //定义通道:打开一个ServerSocketChannel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定地址:为ServerSocketChannel绑定地址信息(主机地址和端口号)
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8080));
        //设置ServerSocketChannel为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        //开启一个选择器
        Selector selector = Selector.open();
        //将ServerSocketChannel通道注册到选择器上,SelectionKey.OP_ACCEPT接收连接进行事件，表示服务器监听到了客户连接，那么服务器可以接收这个连接了
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 需要不断的进行选择操作，选择哪些具有就绪任务的通道信息
        // noinspection InfiniteLoopStatement
        while (true) {
            System.out.println("等待连接，阻塞中.....");
            // 调用选择器select()方法进行选择操作,此方法是一个阻塞方法，如果没有就绪任务的通道此方法阻塞
            int count = selector.select();
            if (count != 0) {
                // 调用Selector的selectedKeys()方法，获取"已选择的键的集合"
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                //遍历集合
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    // 获取每一个SelectionKey
                    SelectionKey selectionKey = iterator.next();
                    // 判断每一个SelectionKey的就绪任务类型，针对不同的任务给出不同的处理方案
                    // 可接收连接任务就绪:客户端已经连接，尚未发送数据
                    if (selectionKey.isAcceptable()) {
                        System.out.println("客户端已经连接，尚未发送数据....");
                        // 获取该SelectionKey所关联的通道
                        ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                        // 获取连接
                        SocketChannel socketChannel = ssc.accept();
                        // 设置非阻塞
                        socketChannel.configureBlocking(false);
                        // 注册到选择器:将该通道注册到选择器上，读就绪事件，表示通道中已经有了可读的数据，可以执行读操作了
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    // 任务就绪:可读取数据的任务就绪
                    else if (selectionKey.isReadable()) {
                        System.out.println("客户端成功发送数据");
                        // 获取该SelectionKey所关联的通道
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        // 读取通道中的数据
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int read = socketChannel.read(buffer);
                        while (read > 0) {
                            // 切换缓冲区的模式:将Buffer从写模式切换到读模式
                            buffer.flip();
                            System.out.println(new String(buffer.array(), 0, read));
                            // 清除缓冲区:清除此缓冲区(数据还在)。位置设置为零，限制设置为容量
                            buffer.clear();
                            read = socketChannel.read(buffer);
                        }
                        //释放资源
                        socketChannel.close();
                    }
                    // 任务处理完毕以后，将SelectionKey从"已选择的键的集合"移除掉
                    iterator.remove();
                }
            }
        }
    }
}