package io.github.ldhai99.easyOrm.autoConfig;


import io.github.ldhai99.easyOrm.executor.IMapper;
import io.github.ldhai99.easyOrm.executor.JdbcTemplateMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(EasyOrmProperties.class)
public class EasyOrmConfig {
    // 自动注入 DataSource Bean
    //生成BaseMapper  bean,全局执行器
    @Bean
    public IMapper setDataSource(DataSource dataSource, EasyOrmProperties easyOrmProperties, NamedParameterJdbcTemplate npjt) {

        //保存全局数据源
        EasyOrmProperties.setDs(dataSource);
        //保存全局执行器
        EasyOrmProperties.mapper= new JdbcTemplateMapper(npjt);
        return EasyOrmProperties.mapper;
    }
}

