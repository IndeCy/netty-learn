package io.netty.example.study.client.dispatcher;

import io.netty.example.study.common.Operation;
import io.netty.example.study.common.OperationResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @Package: io.netty.example.study.client.dispatcher
 * @Author: chenyang
 * @Date: 2020/11/7
 * @Version: 1.0
 */
public class OperationFutureCenter {

    private Map<Long, OperationResultFuture> map = new HashMap<Long, OperationResultFuture>();


    public void add(long streamId, OperationResultFuture future){
        map.put(streamId,future);
    }

    public void set(long streamId, OperationResult result){
        OperationResultFuture future = map.get(streamId);
        if (future != null) {
            future.setSuccess(result);
        }
        map.remove(streamId);

    }
}
