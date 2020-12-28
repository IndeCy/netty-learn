package io.netty.example.study.client.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.common.Operation;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.util.IdUtil;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @Package: io.netty.example.study.client.codec
 * @Author: chenyang
 * @Date: 2020/11/7
 * @Version: 1.0
 */
public class RequestMessageToOperationEncoder extends MessageToMessageEncoder<Operation> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Operation operation, List<Object> out) throws Exception {
        RequestMessage tudo = new RequestMessage(IdUtil.nextId(), operation);

        out.add(tudo);
    }
}
