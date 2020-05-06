package com.liugeng.rpcclientapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.liugeng.rpcframework.rpcclient.client.lb.LoadBalancerType;
import com.liugeng.rpcframework.rpcclient.client.service.ServiceDiscovery;
import com.liugeng.rpcframework.rpcclient.proxy.ProxyFactory;
import com.liugeng.rpcframework.rpcclient.proxy.RpcProxy;
import com.liugeng.rpcframework.service.ExampleService;

@Configuration
public class RpcClientConfig {

    @Value("${rpc.zkAddress}")
    private String zkAddress;

    @Value("${rpc.serviceName}")
    private String serviceName;
    
    @Value("${rpc.serviceAddress}")
    private String address;
    
    @Value("${rpc.loadbalancer.type}")
    private LoadBalancerType loadBalancerType;
    
    @Bean(destroyMethod = "finish")
    public ServiceDiscovery serviceDiscovery() {
        return new ServiceDiscovery(zkAddress);
    }

    @Bean
    public ExampleService exampleService(ServiceDiscovery discovery) {
        LoadBalancerType lbType = loadBalancerType != null ? loadBalancerType : LoadBalancerType.RANDOM;
        RpcProxy<ExampleService> exampleServiceProxy = 
            ProxyFactory.newProxy(serviceName, discovery, ExampleService.class, lbType);
        return exampleServiceProxy.proxyInstance();
    }
    
//    @Bean
    public ExampleService exampleService() {
        RpcProxy<ExampleService> exampleServiceProxy = ProxyFactory.newProxy(address, ExampleService.class);
        return exampleServiceProxy.proxyInstance();
    }
}
