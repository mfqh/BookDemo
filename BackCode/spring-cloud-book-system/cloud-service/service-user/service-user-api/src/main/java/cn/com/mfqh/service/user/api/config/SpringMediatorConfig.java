package cn.com.mfqh.service.user.api.config;


import cn.com.mfqh.service.user.infrastructure.mediatr.core.Mediator;
import cn.com.mfqh.service.user.infrastructure.mediatr.core.Registry;
import cn.com.mfqh.service.user.infrastructure.mediatr.spring.SpringMediator;
import cn.com.mfqh.service.user.infrastructure.mediatr.spring.SpringRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注册中介者模式
 */
@Configuration
public class SpringMediatorConfig {
    private final ApplicationContext applicationContext;

    public SpringMediatorConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public Registry registry() {
        return new SpringRegistry(applicationContext);
    }
    @Bean
    public Mediator mediator(Registry registry) {
        return new SpringMediator(registry);
    }

}
