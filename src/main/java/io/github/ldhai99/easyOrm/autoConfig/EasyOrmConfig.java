package io.github.ldhai99.easyOrm.autoConfig;


import io.github.ldhai99.easyOrm.DbConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(EasyOrmProperties.class)
public class EasyOrmConfig {
   // 自动注入 DataSource Bean
    @Bean("DataSourceEasyOrm")
    public DbConfig setDataSource(DataSource dataSource,EasyOrmProperties easyOrmProperties) {
        DbConfig.setDatabase(easyOrmProperties.getDatabase());
        DbConfig.setDs(dataSource);
        return new DbConfig();
    }

}
