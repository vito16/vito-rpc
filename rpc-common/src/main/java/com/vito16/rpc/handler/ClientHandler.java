package com.vito16.rpc.handler;

import com.vito16.rpc.handler.feture.DefaultFuture;
import com.vito16.rpc.protocol.RpcRequest;
import com.vito16.rpc.protocol.RpcResponse;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author vito
 * @date 2020/11/10
 */
public class ClientHandler extends ChannelDuplexHandler {

    /**
     * 使用Map维护请求对象ID与响应结果Future的映射关系
     */
    private final Map<String, DefaultFuture> futureMap = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcResponse) {
            //获取响应对象
            RpcResponse response = (RpcResponse) msg;
            DefaultFuture defaultFuture = futureMap.get(response.getRequestId());
            defaultFuture.setResponse(response);
        }
        super.channelRead(ctx,msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof RpcRequest) {
            RpcRequest request = (RpcRequest) msg;
            //发送请求对象之前，先把请求ID保存下来，并构建一个与响应Future的映射关系
            futureMap.putIfAbsent(request.getRequestId(), new DefaultFuture());
        }
        super.write(ctx, msg, promise);
    }

    /**
     * 获取响应结果
     *
     * @param requestId
     * @return
     */
    public RpcResponse getRpcResponse(String requestId) {
        RpcResponse response = null;
        try {
            TimeUnit.SECONDS.sleep(5);
            DefaultFuture future = futureMap.get(requestId);
            response = future.getRpcResponse(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //获取成功以后，从map中移除
            futureMap.remove(requestId);
        }
        return response;
    }

}
