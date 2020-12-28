package io.netty.example.study.client.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.study.common.Operation;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

/**
 * @Package: io.netty.example.study.client.codec
 * @Author: chenyang
 * @Date: 2020/11/7
 * @Version: 1.0
 */
public class ClientV1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderProtocolDecoder());
                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderFrameEncoder());

                pipeline.addLast(new RequestMessageToOperationEncoder());
            }
        });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090);
        channelFuture.sync();
        Operation tudo = new OrderOperation(1001, "tudo");

        channelFuture.channel().writeAndFlush(tudo);

        channelFuture.channel().closeFuture().get();
    }
}
