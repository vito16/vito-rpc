package com.vito16.rpc.provider;

import com.vito16.rpc.annotation.VitoService;
import com.vito16.rpc.handler.ServerHandler;
import com.vito16.rpc.protocol.RpcDecoder;
import com.vito16.rpc.protocol.RpcEncoder;
import com.vito16.rpc.protocol.RpcRequest;
import com.vito16.rpc.protocol.RpcResponse;
import com.vito16.rpc.protocol.serialize.JSONSerializer;
import com.vito16.rpc.registry.ServiceRegistry;
import com.vito16.rpc.registry.zookeeper.ZkServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.vito16.rpc.common.Constant.PROTOCOL_PORT;

/**
 * @author vito
 * @date 2020/11/10
 */
@Component
@Slf4j
public class NettyServer implements CommandLineRunner,ApplicationContextAware {

    private EventLoopGroup boss = null;

    private EventLoopGroup worker = null;

    ApplicationContext applicationContext;

    @Autowired
    private ServerHandler serverHandler;

    @Override
    public void run(String... args) throws Exception {
        ServiceRegistry registry = new ZkServiceRegistry();
        start(registry);
    }

    private void start(ServiceRegistry registry) {
        log.info("远程服务start");
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4));
                        pipeline.addLast(new RpcEncoder(RpcResponse.class, new JSONSerializer()));
                        pipeline.addLast(new RpcDecoder(RpcRequest.class, new JSONSerializer()));
                        pipeline.addLast(serverHandler);

                    }
                });
        bind(serverBootstrap, PROTOCOL_PORT);
        try {
            registry.registry(applicationContext.getBeansWithAnnotation(VitoService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果端口绑定失败，端口数+1,重新绑定
     *
     * @param serverBootstrap
     * @param port
     */
    public void bind(final ServerBootstrap serverBootstrap,int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                log.info("端口[ {} ] 绑定成功",port);
            } else {
                log.error("端口[ {} ] 绑定失败", port);
                bind(serverBootstrap, port + 1);
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
