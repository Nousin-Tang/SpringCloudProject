package com.nousin.springcloud.server.storage.framework.config.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据源配置
 *
 * @author Nousin
 * @since 2020/1/4
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfig {
	// 连接池属性
	private String type;
	private String driverClassName;
	private String initialSize;
	private String minIdle;
	private String maxActive;
	private String maxWait;
	private String timeBetweenEvictionRunsMillis;
	private String minEvictableIdleTimeMillis;
	private String validationQuery;
	private String testWhileIdle;
	private String testOnBorrow;
	private String testOnReturn;
	private String filters;

	// 数据库配置
	private DataSourceLink storage;
	private DataSourceLink base;

	@Getter
	@Setter
	static class DataSourceLink {
		private String url;
		private String username;
		private String password;
	}

	/**
	 * 配置[storageDataSource]对应数据源
	 *
	 * @return
	 */
	@Primary
	@Bean(name = "storageDataSource")
	public DataSource storageDataSource() {
		return buildDataSource(storage, "storageDataSource");
	}

	/**
	 * 配置[baseDataSource]对应数据源
	 *
	 * @return
	 */
	@Bean(name = "baseDataSource")
	public DataSource baseDataSource() {
		return buildDataSource(base, "baseDataSource");
	}

	private AtomikosDataSourceBean buildDataSource(DataSourceLink dataSourceLink, String dataSourceName) {
		Properties prop = new Properties();
		prop.put("url", dataSourceLink.getUrl());
		prop.put("username", dataSourceLink.getUsername());
		prop.put("password", dataSourceLink.getPassword());
		prop.put("driverClassName", driverClassName);
		prop.put("filters", filters);
		prop.put("maxActive", maxActive);
		prop.put("initialSize", initialSize);
		prop.put("maxWait", maxWait);
		prop.put("minIdle", minIdle);
		prop.put("timeBetweenEvictionRunsMillis", timeBetweenEvictionRunsMillis);
		prop.put("minEvictableIdleTimeMillis", minEvictableIdleTimeMillis);
		prop.put("validationQuery", validationQuery);
		prop.put("testWhileIdle", testWhileIdle);
		prop.put("testOnBorrow", testOnBorrow);
		prop.put("testOnReturn", testOnReturn);

		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		ds.setXaDataSourceClassName(type);
		ds.setPoolSize(5);
		ds.setXaProperties(prop);
		ds.setUniqueResourceName(dataSourceName);
		ds.setTestQuery("select 1");
		return ds;
	}

}
