package com.vito16.rpc.registry.zookeeper;

import com.vito16.rpc.annotation.VitoService;
import com.vito16.rpc.common.Constant;
import com.vito16.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author vito
 * @date 2020/11/10
 */
@Component
@Slf4j
public class ZkServiceRegistry implements ServiceRegistry {

    CuratorFramework curatorFramework;

    ApplicationContext applicationContext;

    @Autowired
    Environment environment;

//    @Autowired
//    RpcProperties rpcProperties;

    private String hostAddress;

    @Value("${vito.rpc.zk-conn}")
    String zkConn="127.0.0.1:2181";

    @Value("${vito.rpc.provider.host}")
    String providerHost="172.20.10.3";

    @Override
    public void registry(Map<String, Object> outServices) throws Exception {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zkConn)
                .sessionTimeoutMs(Constant.ZK_SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        curatorFramework.start();
        String port = Constant.PROTOCOL_PORT+"";
        hostAddress = providerHost+":"+port;
        log.info("当前服务地址：{}",hostAddress);
        String path = Constant.ZK_CHILDREN_PATH;
        for (Map.Entry<String, Object> beanEntry : outServices.entrySet()) {
            //注册( /path/暴露接口 -> [server:port,server2:port] )
            VitoService vitoService = beanEntry.getValue().getClass().getAnnotation(VitoService.class);
            String serviceInter = vitoService.service().getName();
            String servicePath = path+serviceInter;
            //TODO 考虑并发启动的情况
            Stat s = curatorFramework.checkExists().forPath(servicePath);
            if(s!=null){
                curatorFramework.setData().forPath(servicePath, servicePath.getBytes());
                log.info("服务注册地址更新.{}-{}",beanEntry.getValue(),servicePath);
            }else{
                curatorFramework.create().withMode(CreateMode.PERSISTENT)
                        .forPath(servicePath, hostAddress.getBytes());
                log.info("服务注册.{}-{}",beanEntry.getValue(),servicePath);
            }
        }
        log.info("服务暴露类完成所有注册,总个数:[{}]",outServices.size());

    }
}
