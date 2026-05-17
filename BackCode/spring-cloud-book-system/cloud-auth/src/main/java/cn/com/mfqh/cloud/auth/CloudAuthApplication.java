package cn.com.mfqh.cloud.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableDiscoveryClient
@SpringBootApplication
@EnableRedisHttpSession
public class CloudAuthApplication {

    public static void main(String[] args){
        SpringApplication.run(CloudAuthApplication.class);
    }
}
