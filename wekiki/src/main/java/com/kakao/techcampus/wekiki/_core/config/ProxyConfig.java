package com.kakao.techcampus.wekiki._core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Slf4j
@Configuration
public class ProxyConfig {
    private static final String PROXY_HOST = "krmp-proxy.9rum.cc";
    private static final int PROXY_PORT = 3128;

    @Bean
    @Profile("prodkrampoline")
    public RestTemplate restTemplate() {
        log.info("krampoline 배포용 프록시 서버");
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
        requestFactory.setProxy(proxy);
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);
        return new RestTemplate(requestFactory);
    }
}
