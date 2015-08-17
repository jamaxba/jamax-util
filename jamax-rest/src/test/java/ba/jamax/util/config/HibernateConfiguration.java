package ba.jamax.util.config;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement(proxyTargetClass=true)
@ComponentScan({ "ba.jamax.util" })
@PropertySource(value = { "classpath:config/hibernate.properties" })
public class HibernateConfiguration {

	@Autowired
	private Environment environment;

	@Bean
	public LocalSessionFactoryBean sessionFactory() throws NumberFormatException, IllegalStateException, PropertyVetoException, SQLException {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] { "ba.jamax.util" });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	@Bean
	public DataSource dataSource() throws IllegalStateException, PropertyVetoException, NumberFormatException, SQLException {
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(environment.getRequiredProperty("jdbc.driverClassName"));
		dataSource.setJdbcUrl(environment.getRequiredProperty("jdbc.url"));
		dataSource.setUser(environment.getRequiredProperty("jdbc.username"));
		dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
		dataSource.setMinPoolSize(Integer.valueOf(environment.getRequiredProperty("db.minPoolSize")));
		dataSource.setMaxPoolSize(Integer.valueOf(environment.getRequiredProperty("db.maxPoolSize")));
		dataSource.setMaxStatements(Integer.valueOf(environment.getRequiredProperty("db.maxStatements")));
		dataSource.setIdleConnectionTestPeriod(Integer.valueOf(environment.getRequiredProperty("db.idleConnectionTestPeriod")));
		dataSource.setLoginTimeout(Integer.valueOf(environment.getRequiredProperty("db.loginTimeout")));
		dataSource.setPreferredTestQuery(environment.getRequiredProperty("db.preferredTestQuery"));
		return dataSource;
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect",environment.getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql",environment.getRequiredProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql",environment.getRequiredProperty("hibernate.format_sql"));
		properties.put("hibernate.default_schema",environment.getRequiredProperty("hibernate.default_schema"));
		properties.put("hibernate.default_catalog",environment.getRequiredProperty("hibernate.default_catalog"));
		properties.put("hibernate.prepare_sql",environment.getRequiredProperty("hibernate.prepare_sql"));
		properties.put("hibernate.hbm2ddl.auto",environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
		properties.put("hibernate.jdbc.batch_size",environment.getRequiredProperty("hibernate.jdbc.batch_size"));
		properties.put("hibernate.jdbc.fetch_size",environment.getRequiredProperty("hibernate.jdbc.fetch_size"));
		properties.put("hibernate.max_fetch_depth",environment.getRequiredProperty("hibernate.max_fetch_depth"));
		properties.put("hibernate.generate_statistics", environment.getRequiredProperty("hibernate.generate_statistics"));
		properties.put("hibernate.schema",environment.getRequiredProperty("hibernate.schema"));

		return properties;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		return txManager;
	}
}