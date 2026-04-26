package cn.com.mfqh.service.user.infrastructure.mediatr.spring;

import cn.com.mfqh.service.user.infrastructure.mediatr.core.RequestHandler;
import org.springframework.context.ApplicationContext;

/**
 * A wrapper around a RequestHandler
 * 只存储class对象 解决：【懒加载】+【解决 Spring 生命周期问题】+【保证每次获取的都是最新的 Bean】
 **/
public class RequestHandlerProvider<T extends RequestHandler<?, ?>> {
    private ApplicationContext applicationContext;
    private Class<T> clazz;

    public RequestHandlerProvider(ApplicationContext applicationContext, Class<T> clazz) {
        this.applicationContext = applicationContext;
        this.clazz = clazz;
    }

    public T handler() {
        return this.applicationContext.getBean(clazz);
    }
}
