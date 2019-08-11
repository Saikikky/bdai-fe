package com.bdai.fe.config;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
定义了Commons的HttpClientBean 直接@Autowired使用

*/
@Configuration
public class HttpClientBean {

    @Bean
    public HttpClient getHttpClient() {
        return new HttpClient(new MultiThreadedHttpConnectionManager());
    }

}
