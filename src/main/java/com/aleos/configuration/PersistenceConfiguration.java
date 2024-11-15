package com.aleos.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import liquibase.integration.spring.SpringLiquibase;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:dbconfig.properties")
@EnableTransactionManagement
@AllArgsConstructor
public class PersistenceConfiguration {

    private final Environment env;

    /**
     * Configures the DataSource bean using HikariCP.
     *
     * @return DataSource configured with HikariCP settings
     */
    @Bean
    public DataSource dataSource() {
        var hikariConfig = configureHikariPool();
        return new HikariDataSource(hikariConfig);
    }

    /**
     * Configures and returns the LocalContainerEntityManagerFactoryBean.
     * The EntityManagerFactory manages entity persistence and interactions with the database.
     * This configuration sets up the factory to work with Hibernate as the JPA provider
     * and integrates it with the defined DataSource and Hibernate properties.
     *
     * @return LocalContainerEntityManagerFactoryBean configured for the application
     */
    @Bean
    @DependsOn("springLiquibase")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPackagesToScan("com.aleos.model");
        factory.setJpaProperties(additionalHibernateProperties());

        return factory;
    }

    /**
     * Configures the HikariCP connection pool.
     *
     * @return HikariConfig with pool settings
     */
    private HikariConfig configureHikariPool() {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(env.getProperty("POSTGRES_URL"));
        hikariConfig.setUsername(env.getProperty("POSTGRES_USER"));
        hikariConfig.setPassword(env.getProperty("POSTGRES_PASSWORD"));

        hikariConfig.setMaximumPoolSize(Integer.parseInt(env.getProperty("maximumPoolSize", "10")));
        hikariConfig.setMinimumIdle(Integer.parseInt(env.getProperty("minimumIdle", "3")));
        hikariConfig.setIdleTimeout(Long.parseLong(env.getProperty("idleTimeout", "300000")));
        hikariConfig.setConnectionTimeout(Long.parseLong(env.getProperty("connectionTimeout", "30000")));
        hikariConfig.setMaxLifetime(Long.parseLong(env.getProperty("maxLifetime", "1800000")));

        return hikariConfig;
    }

    /**
     * Provides additional Hibernate properties.
     *
     * @return Properties object containing Hibernate settings
     */
    private Properties additionalHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        properties.setProperty("hibernate.default_schema", env.getProperty("hibernate.default_schema"));
        properties.setProperty("hibernate.physical_naming_strategy", env.getProperty("hibernate.physical_naming_strategy"));
        properties.setProperty("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));
        properties.setProperty("hibernate.session.events.log.LOG_QUERIES_SLOWER_THAN_MS", env.getProperty("hibernate.session.events.log.LOG_QUERIES_SLOWER_THAN_MS"));

        // Instruct Hibernate to use the provided DataSource
        properties.setProperty("hibernate.connection.provider_class", env.getProperty("hibernate.connection.provider_class"));

        return properties;
    }

    /**
     * Configures the transaction manager for managing transactions.
     *
     * @param entityManagerFactory Injected EntityManagerFactory
     * @return PlatformTransactionManager configured for JPA transactions
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

    /**
     * Configures and returns a SpringLiquibase bean.
     * The SpringLiquibase bean is set up with a specified DataSource and
     * Liquibase changelog file. This bean initializes the Liquibase integration
     * to manage database migrations.
     *
     * @param dataSource the DataSource to be used by SpringLiquibase
     * @return a configured SpringLiquibase instance
     */
    @Bean
    public SpringLiquibase springLiquibase(DataSource dataSource) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(dataSource);
        springLiquibase.setChangeLog("db/changelog/db.changelog-master.yml");
        springLiquibase.setShouldRun(true);
        return springLiquibase;
    }
}
