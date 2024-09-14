package io.github.ldhai99.easyOrm.autoConfig;

import io.github.ldhai99.easyOrm.page.PageData;
import io.github.ldhai99.easyOrm.page.MysqlPageData;
import io.github.ldhai99.easyOrm.executor.JdbcTemplateExecutor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;


@ConfigurationProperties(prefix="easyorm",ignoreInvalidFields = true)
public class EasyOrmProperties {
    private String database="mysql";
    //全局数据源
    public static DataSource ds=null;
    //全局执行器
    public static JdbcTemplateExecutor executor=null;
    private static PageData pageData;

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

    public PageData getDialect() {

        if(pageData ==null){
            if(database.equalsIgnoreCase("mysql")) {
                return  new MysqlPageData();
            }
        }

        return pageData;
    }






}
