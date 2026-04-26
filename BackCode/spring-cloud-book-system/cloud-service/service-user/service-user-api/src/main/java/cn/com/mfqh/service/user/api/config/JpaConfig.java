package cn.com.mfqh.service.user.api.config;


import cn.com.mfqh.common.db.config.DateSourceParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@ComponentScan(basePackages = {"cn.com.mfqh.service.user.domain.model"})
@EnableJpaRepositories(
        basePackages = "cn.com.mfqh.service.user.domain.repository",
        entityManagerFactoryRef = DateSourceParameters.ServiceUserUpdate.SERVICE_USER_UPDATE_ENTITY_MANGER_FACTORY,
        transactionManagerRef = DateSourceParameters.ServiceUserUpdate.SERVICE_USER_UPDATE_TRANSACTION_MANGER_FACTORY
)
public class JpaConfig {
    @Autowired
    Environment env;

    /**
     * 分布式事务启用seata，暂不使用
     * @return
     */
//    @Bean
//    @ConfigurationProperties(prefix = DateSourceParameters.ServiceUserUpdate.SERVICE_USER_UPDATE_DATA_SOURCE_PREFIX)
//    public DataSource origOaDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Primary
//    @Bean(name = DateSourceParameters.ServiceUserUpdate.SERVICE_USER_UPDATE_DATA_SOURCE)
//    public DataSource updateDataSource() {
//        return new DataSourceProxy(origOaDataSource());
//    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = DateSourceParameters.ServiceUserUpdate.SERVICE_USER_UPDATE_DATA_SOURCE_PREFIX)
    public DataSource updateDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = {
            "entityManagerFactory", // 🔥 强行注册默认名，解决报错
            DateSourceParameters.ServiceUserUpdate.SERVICE_USER_UPDATE_ENTITY_MANGER_FACTORY
    })
    public LocalContainerEntityManagerFactoryBean entityMangerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(updateDataSource());
        em.setPackagesToScan(
                new String[]{"cn.com.mfqh.service.user.domain"});

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.dialect",
                env.getProperty("spring.jpa.hibernate.dialect"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Primary
    @Bean(name = DateSourceParameters.ServiceUserUpdate.SERVICE_USER_UPDATE_TRANSACTION_MANGER_FACTORY)
    public PlatformTransactionManager transactionMangerFactory() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityMangerFactory().getObject());
        return transactionManager;
    }
}
