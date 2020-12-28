package io.netty.example.study.server.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.study.server.codec.handler.OrderProcessHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

/**
 * @Package: io.netty.example.study.server.codec
 * @Author: chenyang
 * @Date: 2020/11/7
 * @Version: 1.0
 */
public class Server {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        serverBootstrap.group(new NioEventLoopGroup());

        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolDecoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProcessHandler());


                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
            }
        });

        ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();

        channelFuture.channel().closeFuture().get();
    }
}
