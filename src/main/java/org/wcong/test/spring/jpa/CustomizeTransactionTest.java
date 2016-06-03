package org.wcong.test.spring.jpa;

import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author wcong<wc19920415@gmail.com>
 * @since 16/4/19
 */
@Configuration
@EnableJpaRepositories
public class CustomizeTransactionTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.register(CustomizeTransactionTest.class);
        annotationConfigApplicationContext.refresh();
        DbStockRepository dbStockRepository = annotationConfigApplicationContext.getBean(DbStockRepository.class);
        System.out.println(dbStockRepository.count());
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
        return embeddedDatabaseBuilder.setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean containerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        containerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        containerEntityManagerFactoryBean.setDataSource(dataSource());
        containerEntityManagerFactoryBean.setPackagesToScan("org.wcong.test.spring.jpa");
        containerEntityManagerFactoryBean.getJpaPropertyMap().put("hibernate.implicit_naming_strategy", ImplicitNamingStrategyJpaCompliantImpl.class.getName());
        containerEntityManagerFactoryBean.afterPropertiesSet();
        return containerEntityManagerFactoryBean.getObject();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }


}
