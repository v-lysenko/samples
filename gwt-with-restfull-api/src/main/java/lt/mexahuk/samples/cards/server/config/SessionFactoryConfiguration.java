package lt.mexahuk.samples.cards.server.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
class SessionFactoryConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SessionFactoryConfiguration.class);

    // @formatter:off
    @Bean(destroyMethod = "close")
    public DataSource dataSource(
            @Value("${db.driver}") String driver,
            @Value("${db.url}") String url,
            @Value("${db.user}") String user,
            @Value("${db.password}") String password,
            @Value("${db.testsql:SELECT 1}") String testQuery) throws PropertyVetoException {
        log.info("Setup datasource");
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setValidationQuery(testQuery);
//		dataSource.setAutoCommitOnClose(false);
//		dataSource.setMaxIdleTime(maxIdleTime);

        return dataSource;
    }
    // @formatter:on

    // @formatter:off
    @Bean
    Properties hibernateProperties(
            @Value("${hibernate.dialect}") String dialect,
            @Value("${hibernate.hbm2ddl.auto}") String hbm2ddl,
            @Value("${hibernate.show_sql}") boolean showSql) {

        Properties properties = new Properties();
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.show_sql", showSql);

        properties.put("hibernate.hbm2ddl.auto", hbm2ddl);
//		properties.put("hibernate.jdbc.fetch_size", fetchSize);
        properties.put("hibernate.order_updates", true);
//		properties.put("hibernate.cache.use_query_cache", true);

//		properties.put("hibernate.cache.use_second_level_cache", true);
//		properties.put("org.hibernate.envers.audit_table_prefix", "AUDIT_");
//		properties.put("org.hibernate.envers.audit_table_suffix", "");
        return properties;
    }
    // @formatter:on

    @Bean
    SessionFactory sessionFactory(DataSource dataSource, @Qualifier("hibernateProperties") Properties properties) throws Exception {

        log.info("Setup session factory");

        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setPackagesToScan("lt.mexahuk.samples.cards.server.model");
//		sessionFactoryBean.setConfigLocation(new ClassPathResource("hibernate.cfg.xml"));
        sessionFactoryBean.setHibernateProperties(properties);
        sessionFactoryBean.afterPropertiesSet();
        SessionFactory sessionFactory = sessionFactoryBean.getObject();
        return sessionFactory;
    }

    @Bean
    PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
        log.info("Setup transaction manager");
        return new HibernateTransactionManager(sessionFactory);
    }

}
