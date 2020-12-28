package io.netty.example.study.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.example.study.client.dispatcher.OperationFutureCenter;
import io.netty.example.study.common.ResponseMessage;

/**
 * @Package: io.netty.example.study.client.handler
 * @Author: chenyang
 * @Date: 2020/11/7
 * @Version: 1.0
 */
public class ResponseDispatcherHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    public ResponseDispatcherHandler(OperationFutureCenter operationFutureCenter){
        this.center = operationFutureCenter;
    }

    private OperationFutureCenter center;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage msg) throws Exception {
        center.set(msg.getMessageHeader().getStreamId(),msg.getMessageBody());
    }
}
