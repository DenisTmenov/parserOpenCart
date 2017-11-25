package com.denis.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.denis.utils.PropertiesClass;

@Configuration
@ComponentScan(basePackages = { "com.denis.dto" })
@EnableTransactionManagement
public class ProjectConfiguration {
	private Properties properties;

	private static class DataSourceHolder {
		private static final ProjectConfiguration HOLDER_INSTANCE = new ProjectConfiguration();
	}

	public static ProjectConfiguration getInstance() {
		return DataSourceHolder.HOLDER_INSTANCE;
	}

	public Connection getConnection() throws SQLException {
		return getDataSource().getConnection();
	}

	@Bean("dataSource")
	public DataSource getDataSource() {
		properties = PropertiesClass.getSettings("dbConfig");

		BasicDataSource basicDS = new BasicDataSource();

		basicDS.setDriverClassName(properties.getProperty("db.driver"));
		basicDS.setUrl(properties.getProperty("db.url"));
		basicDS.setUsername(properties.getProperty("db.user"));
		basicDS.setPassword(properties.getProperty("db.password"));

		if (properties.getProperty("db.ssl") != null) {
			basicDS.setConnectionProperties("useSSL=" + properties.getProperty("db.ssl"));
		}

		return basicDS;
	}

	@Bean
	public SessionFactory getSessionFactory(DataSource dataSource) {

		LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource);

		builder.addProperties(getHibernateProperties());
		builder.scanPackages("com.denis.dto");

		return builder.buildSessionFactory();
	}

	private Properties getHibernateProperties() {
		properties.put("hibernate.dialect", properties.getProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.format_sql", "true");

		return properties;
	}

	@Bean
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager(sessionFactory);
		return hibernateTransactionManager;
	}

	////////////////

	public static void closeDbResources(Connection connection, PreparedStatement statement, ResultSet resultSet) {
		closeResultSet(resultSet);
		closeStatement(statement);
		closeConnection(connection);
	}

	public static void closeDbResources(Connection connection, PreparedStatement statement) {
		closeStatement(statement);
		closeConnection(connection);
	}

	private static void closeConnection(Connection connection) {
		if (connection != null) {

			try {
				connection.close();
			} catch (SQLException e) {
			}

		}
	}

	private static void closeStatement(PreparedStatement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
			}
		}
	}

	private static void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
			}
		}
	}

}