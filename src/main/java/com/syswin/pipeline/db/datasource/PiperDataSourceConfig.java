package com.syswin.pipeline.db.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 115477 on 2018/11/27.
 */
@Configuration
@MapperScan(basePackages = {"com.syswin.pipeline.db.repository"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class PiperDataSourceConfig {

	@Value("${spring.datasource.pipeline.pipelineMapperLocations}")
	private String linjuMapperLocations;
	@Value("${mybatis.config-location}")
	private String mybitsConfig;


	@Autowired
	MybatisProperties properties;

	String mapperLocations = "/META-INF/mapper/*.xml";

//	String typeAliasesPackage = "com.syswin.ps.sdk.admin.platform.entity";

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
		Resource[] resources1 = resolver.getResources(linjuMapperLocations);
		VFS.addImplClass(SpringBootVFS.class);
		List<Resource> resourceList = new ArrayList<>();
		resourceList.addAll(Arrays.asList(resources1));

		Resource[] temp = resolver.getResources(mapperLocations);
		resourceList.addAll(Arrays.asList(temp));
		// 配置mapper文件位置
		sqlSessionFactoryBean.setMapperLocations(resourceList.stream().toArray(Resource[]::new));
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
