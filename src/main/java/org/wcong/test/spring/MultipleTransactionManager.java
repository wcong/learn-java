package org.wcong.test.spring;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/2/23
 */
@Configuration
@EnableTransactionManagement
public class MultipleTransactionManager {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
		annotationConfigApplicationContext.register(MultipleTransactionManager.class);
		annotationConfigApplicationContext.refresh();
	}


	@Bean
	public DataSourceTransactionManager getDataSourceTransactionManager() {
		return new DataSourceTransactionManager();
	}

	@Bean
	public DataSource getDataSource() {
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/wxstock?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE&zeroDateTimeBehavior=convertToNull");
		basicDataSource.setUsername("root");
		return basicDataSource;
	}

}
