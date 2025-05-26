package GBFH.GBFH_BE.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "GBFH.GBFH_BE.repository.sub",
        entityManagerFactoryRef = "subEntityManager",
        transactionManagerRef = "subTransactionManager"
)
public class SubDataSourceConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.sub")
    public DataSource subDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean subEntityManager(
            EntityManagerFactoryBuilder builder) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update"); // <- 여기가 핵심
        properties.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");

        return builder
                .dataSource(subDataSource())
                .packages("GBFH.GBFH_BE.entity.sub")
                .persistenceUnit("sub")
                .properties(properties) // 추가
                .build();
    }

    @Bean
    public PlatformTransactionManager subTransactionManager(
            @Qualifier("subEntityManager") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
