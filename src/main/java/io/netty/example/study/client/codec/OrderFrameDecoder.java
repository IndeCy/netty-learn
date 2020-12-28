package io.netty.example.study.client.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Package: io.netty.example.study.server
 * @Author: chenyang
 * @Date: 2020/11/7
 * @Version: 1.0
 */
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {

    public OrderFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 1,0,1);
    }

}
