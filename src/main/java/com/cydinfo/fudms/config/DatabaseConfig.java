package com.cydinfo.fudms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {


    /**
     * dataSource 설정을 Bean 에 주입
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mariadb://192.168.128.177:3406/cydinfo");
        dataSource.setUsername("cydinfo");
        dataSource.setPassword("cydinfo123!@#");

        return dataSource;
    }
}
