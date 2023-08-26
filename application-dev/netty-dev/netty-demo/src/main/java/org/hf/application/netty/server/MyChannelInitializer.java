package org.hf.application.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.hf.application.netty.codec.MyDecoder;
import org.hf.application.netty.codec.MyEncoder;

/**
 * <p> ChannelHandler的初始化 </p >
 * @author hufei
 * @date 2022/8/6 20:43
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 这里声明了处理器, 那么MyCustomProtocolChannelHandler需要@ChannelHandler.Sharable注解修饰,否则会报错,不同链接不能多次添加同一个处理器对象
     */
    private static final MyCustomProtocolChannelHandler INSTANCE = new MyCustomProtocolChannelHandler();

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        //将业务处理器加入到列表中
        // 不推荐 原因参考{@link org.hf.application.netty.server.MyCustomChannelHandler}
//        socketChannel.pipeline().addLast(new MyChannelHandler());
        //这里可以接着调用 addLast(), 将需要处理的业务都加入进来, 按照这里加载的处理器进行数据处理
//        socketChannel.pipeline().addLast(new MyCustomChannelHandler());
        // 这里是为了解决粘包/拆包的问题
        socketChannel.pipeline().addLast(new MyDecoder()).addLast(new MyEncoder()).addLast(INSTANCE);
    }
}