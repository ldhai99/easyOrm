package io.github.ldhai99.easyOrm.autoConfig;

import io.github.ldhai99.easyOrm.Dialect.Dialect;
import io.github.ldhai99.easyOrm.Dialect.MysqlDialect;
import io.github.ldhai99.easyOrm.executor.JdbcTemplateMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;


@ConfigurationProperties(prefix="easyorm",ignoreInvalidFields = true)
public class EasyOrmProperties {
    private String database="mysql";
    //全局数据源
    public static DataSource ds=null;
    //全局执行器
    public static JdbcTemplateMapper mapper=null;
    private static Dialect dialect;

    public void setDatabase(String database) {
        this.database = database;
    }
    public String getDatabase() {
        return database;
    }


    public static DataSource getDs() {
        return ds;
    }

    public static void setDs(DataSource ds) {
        EasyOrmProperties.ds = ds;
    }

    public  Dialect getDialect() {

        if(dialect==null){
            if(database.equalsIgnoreCase("mysql")) {
                return  new MysqlDialect();
            }
        }

        return dialect;
    }






}
