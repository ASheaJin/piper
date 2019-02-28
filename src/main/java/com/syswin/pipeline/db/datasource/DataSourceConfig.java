package com.syswin.pipeline.db.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by 115477 on 2018/11/27.
 */
@Configuration
@MapperScan(basePackages = { "com.syswin.pipeline.db.repository" }, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {

    @Value("${spring.datasource.pipeline.pipelineMapperLocations}")
    private String linjuMapperLocations;
    @Value("${mybatis.config-location}")
    private String mybitsConfig;


    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.pipeline")
    @Primary
    public DataSource getDataSource() {
        return new DruidDataSource();
    }


    /**
     * SqlSessionFactory配置
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource(mybitsConfig));
        // 配置mapper文件位置
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(linjuMapperLocations));
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 配置事物管理器
     *
     * @return
     */
    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager getTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }
}
