package com.nousin.springcloud.server.storage.framework.config.datasource;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import lombok.extern.slf4j.Slf4j;

/**
 * Storage SqlSessionTemplateConfig
 * 下面的sqlSessionTemplateRef 值需要和生成的SqlSessionTemplate bean name相同，如果没有指定name,那么就是方法名
 *
 * @author Nousin
 * @since 2020/1/4
 */
@Slf4j
@Configuration
@MapperScan(basePackages = { "com.nousin.springcloud.server.storage.web.dao.storage",
		"com.nousin.springcloud.server.storage.framework.security.dao" }, sqlSessionTemplateRef = "storageSqlSessionTemplate")
public class StorageSqlSessionTemplateConfig {

	@Value("${mybatis.mapper-locations}")
	private String mapperLocations;

	/**
	 * 自定义sqlSessionFactory配置（因为没有用到MybatisAutoConfiguration自动配置类，需要手动配置）
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Primary
	public SqlSessionFactory storageSqlSessionFactory(@Qualifier("storageDataSource") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		//如果重写了 SqlSessionFactory 需要在初始化的时候手动将 mapper 地址 set到 factory 中，否则会报错：
		//org.apache.ibatis.binding.BindingException: Invalid bound statement (not found)
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
		bean.setVfs(SpringBootVFS.class);
		return bean.getObject();
	}

	/**
	 * SqlSessionTemplate 是 SqlSession接口的实现类，是spring-mybatis中的，实现了SqlSession线程安全
	 *
	 * @param sqlSessionFactory
	 * @return
	 */
	@Bean
	@Primary
	public SqlSessionTemplate storageSqlSessionTemplate(@Qualifier("storageSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
