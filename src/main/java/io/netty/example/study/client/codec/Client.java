package io.netty.example.study.client.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.study.client.codec.OrderFrameDecoder;
import io.netty.example.study.client.codec.OrderFrameEncoder;
import io.netty.example.study.client.codec.OrderProtocolDecoder;
import io.netty.example.study.client.codec.OrderProtocolEncoder;
import io.netty.example.study.common.Operation;
import io.netty.example.study.common.OperationResult;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.util.IdUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetAddress;
import java.util.concurrent.ExecutionException;

/**
 * @Package: io.netty.example.study.client.codec
 * @Author: chenyang
 * @Date: 2020/11/7
 * @Version: 1.0
 */
public class Client {

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
            }
        });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090);
        channelFuture.sync();
        RequestMessage tudo = new RequestMessage(IdUtil.nextId(), new OrderOperation(1001, "tudo"));

        channelFuture.channel().writeAndFlush(tudo);

        channelFuture.channel().closeFuture().get();
    }
}
