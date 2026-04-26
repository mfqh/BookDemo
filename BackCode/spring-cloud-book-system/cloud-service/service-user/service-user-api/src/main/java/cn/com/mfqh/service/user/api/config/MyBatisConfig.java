package cn.com.mfqh.service.user.api.config;

import cn.com.mfqh.common.db.config.DateSourceParameters;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import javax.sql.DataSource;

@Configuration
@ComponentScan
@MapperScan(basePackages = "cn.com.mfqh.service.user.query.mapper",
        sqlSessionFactoryRef = DateSourceParameters.ServiceUserQuery.SERVICE_USER_QUERY_SQL_SESSION_FACTORY,
        sqlSessionTemplateRef = DateSourceParameters.ServiceUserQuery.SERVICE_USER_QUERY_SQL_SESSION_TEMPLATE)
public class MyBatisConfig {

    @Autowired
    private ResourceLoader resourceLoader;


    /**
     * 生成数据源.
     */
    @Bean(name = DateSourceParameters.ServiceUserQuery.SERVICE_USER_QUERY_DATA_SOURCE)
    @ConfigurationProperties(prefix = DateSourceParameters.ServiceUserQuery.SERVICE_USER_QUERY_DATA_SOURCE_PREFIX)
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 创建 SqlSessionFactory
     */
    @Bean(name = DateSourceParameters.ServiceUserQuery.SERVICE_USER_QUERY_SQL_SESSION_FACTORY)
    public SqlSessionFactory sqlSessionFactory(@Qualifier(DateSourceParameters.ServiceUserQuery.SERVICE_USER_QUERY_DATA_SOURCE) DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        bean.setMapperLocations(ResourcePatternUtils.getResourcePatternResolver(resourceLoader).
                getResources("classpath:mappers/*.xml"));
        //bean.setPlugins(new Interceptor[]{new MybatisInterceptor(currentUserProvider)});

        return bean.getObject();
    }

    /**
     * 创建SqlSessionFactoryTemplate
     */
    @Bean(name = DateSourceParameters.ServiceUserQuery.SERVICE_USER_QUERY_SQL_SESSION_TEMPLATE)
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier(DateSourceParameters.ServiceUserQuery.SERVICE_USER_QUERY_SQL_SESSION_FACTORY) SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
