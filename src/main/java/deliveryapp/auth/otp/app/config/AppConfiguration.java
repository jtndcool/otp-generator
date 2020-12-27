package deliveryapp.auth.otp.app.config;


import javax.sql.DataSource;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = "delivery.auth.otp.app")
@PropertySource("classpath:query.properties")
public class AppConfiguration {
	

	
	@Value("${spring.datasource.driver-class-name}")
	private String driverName;
	
	@Value("${spring.datasource.url}")
	private String dbUrl;
	
	@Value("${spring.datasource.username}")
	private String userName;
	
	@Value("${spring.datasource.password}")
	private String passWord;
	
	@Value("${spring.datasource.minConnections}")
	private String minConnections;
	
	@Value("${spring.datasource.maxConnections}")
	private String maxConnections;
	
	@Value("${spring.datasource.partitions}")
	private String partitions;
	
	private static final org.slf4j.Logger logger  = LoggerFactory.getLogger(AppConfiguration.class);
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	
	@Bean(name = "mappingDataSource")
	@Qualifier("mappingDataSource")
	public DataSource mappingDataSource() {
		
		BoneCPConfig boneCPConfig = new BoneCPConfig();
		boneCPConfig.setJdbcUrl(dbUrl);
		boneCPConfig.setUsername(userName);
		boneCPConfig.setPassword(passWord);
		
		boneCPConfig.setMinConnectionsPerPartition(Integer.parseInt(minConnections));
		boneCPConfig.setMaxConnectionsPerPartition(Integer.parseInt(maxConnections));
		boneCPConfig.setPartitionCount(Integer.parseInt(partitions));
		
		BoneCPDataSource dataSource = new BoneCPDataSource(boneCPConfig);
		
		dataSource.setDefaultAutoCommit(Boolean.TRUE);
		dataSource.setDriverClass(driverName);
		
		return dataSource;
	}
	
	@Bean(name = "mappingNamedParameterJdbcTemplate")
	@Qualifier("mappingNamedParameterJdbcTemplate")
	public NamedParameterJdbcTemplate getMappingNamedParameterJdbcTemplate() {
		
		logger.info("Trying to establish a connection");
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(mappingDataSource());
		logger.info("Connection Established successfully");
		return namedParameterJdbcTemplate;
	}
	

}
