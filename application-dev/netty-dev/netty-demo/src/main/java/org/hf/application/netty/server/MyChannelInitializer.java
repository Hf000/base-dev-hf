package org.hf.application.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/8/6 20:43
*/
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        //将业务处理器加入到列表中
        //这里可以接着调用 addLast(), 将需要处理的业务都加入进来
        socketChannel.pipeline().addLast(new MyChannelHandler());
    }
}