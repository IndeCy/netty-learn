package io.netty.example.study.server.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @Package: io.netty.example.study.server.codec
 * @Author: chenyang
 * @Date: 2020/11/7
 * @Version: 1.0
 */
public class OrderFrameEncoder extends LengthFieldPrepender {

    public OrderFrameEncoder() {
        super(1);
    }
}
