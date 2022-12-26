package main.configurations;

import java.util.Properties;
import java.util.Random;
import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class PersistenceJPAConfiguration {
	
	private Random rand=new Random();
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		
		LocalContainerEntityManagerFactoryBean emfb=new LocalContainerEntityManagerFactoryBean();
		emfb.setDataSource(mainDataSource());
		emfb.setPackagesToScan("main.persistence");	
		JpaVendorAdapter adapter=new HibernateJpaVendorAdapter();
		emfb.setJpaVendorAdapter(adapter);
		emfb.setJpaProperties(additionalProperties());		
		return emfb;
	}
	
	
	
	@Bean
	public DataSource mainDataSource() {
		
		DataSourceBuilder dataSourceBuilder=DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.postgresql.Driver");
		dataSourceBuilder.url("jdbc:postgresql://localhost:5432/SpringReviewService");
		dataSourceBuilder.username("postgres");
		dataSourceBuilder.password("password");
		return dataSourceBuilder.build();
	
	}
	
	
	private Properties additionalProperties() {
		
		Properties properties=new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.setProperty("hibernate.auto_quote_keyword", "true");
		return properties;
		
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
	    return transactionManager;
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
	    return new PersistenceExceptionTranslationPostProcessor();
	}
	
	@Bean
	public TransactionTemplate transactionTemplate() {		
		TransactionTemplate transactionTemplate=new TransactionTemplate(transactionManager());
		return transactionTemplate;		
	}
	
}