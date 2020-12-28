package io.netty.example.study.client.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.study.client.dispatcher.OperationFutureCenter;
import io.netty.example.study.client.dispatcher.OperationResultFuture;
import io.netty.example.study.client.handler.ResponseDispatcherHandler;
import io.netty.example.study.common.Operation;
import io.netty.example.study.common.OperationResult;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.util.IdUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

/**
 * @Package: io.netty.example.study.client.codec
 * @Author: chenyang
 * @Date: 2020/11/7
 * @Version: 1.0
 */
public class ClientV2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(new NioEventLoopGroup());
        OperationFutureCenter operationFutureCenter = new OperationFutureCenter();
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolDecoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                pipeline.addLast(new ResponseDispatcherHandler(operationFutureCenter));
            }
        });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090);
        channelFuture.sync();
        long streamId = IdUtil.nextId();
        RequestMessage tudo = new RequestMessage(streamId, new OrderOperation(1001, "tudo"));

        OperationResultFuture future = new OperationResultFuture();

        operationFutureCenter.add(streamId, future);

        channelFuture.channel().writeAndFlush(tudo);

        System.out.println("operation result before");

        OperationResult operationResult = future.get();

        System.out.println("operation result="+operationResult);

        channelFuture.channel().closeFuture().get();
    }
}
