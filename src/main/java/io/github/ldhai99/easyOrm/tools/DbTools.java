package io.github.ldhai99.easyOrm.tools;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.executor.JdbcTemplateExecutor;
import io.github.ldhai99.easyOrm.page.MysqlPageSql;
import io.github.ldhai99.easyOrm.page.MysqlPageSqlById;
import io.github.ldhai99.easyOrm.page.MysqlPageSqlByStartId;
import io.github.ldhai99.easyOrm.page.PageSql;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DbTools {

    private static DataSource dataSource;
    private static String database = "mysql";
    private static NamedParameterJdbcTemplate template;

    //全局执行器
    public static JdbcTemplateExecutor executor = null;
    //全局默认翻页语句生成器
    public static PageSql pageSql;

    public static PageSql pageSqlNormal;
    public static PageSql pageSqlById;
    public static PageSql pageSqlByStartId;


    //获取连接
    public static Connection getConnection() {
        Connection conn = null;
        try {
            if (dataSource != null)
                conn = dataSource.getConnection();
            else
                conn = getDataSource().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    //获取线程数据库连接
    public static Connection getThreadConnection(DataSource dataSource) {
        return DataSourceUtils.getConnection(dataSource);
    }

    //获取全局执行器------------------------------------------------------------------
    public static JdbcTemplateExecutor getExecutor() {
        if (DbTools.executor == null)
            DbTools.createExecutor();
        return DbTools.executor;
    }

    //保存全局执行器--本地数据源
    public static void createExecutor() {
        if (DbTools.executor == null) {
            DbTools.template = new NamedParameterJdbcTemplate(DbTools.getDataSource());
            DbTools.executor = new JdbcTemplateExecutor(DbTools.template);
        }

    }

    //保存全局执行器--外部数据源
    public static void createExecutor(DataSource dataSource) {
        if (DbTools.executor == null) {
            DbTools.template = new NamedParameterJdbcTemplate(dataSource);
            DbTools.executor = new JdbcTemplateExecutor(DbTools.template);
        }
    }

    //保存全局执行器---外部NamedParameterJdbcTemplate
    public static void createExecutor(NamedParameterJdbcTemplate npjt) {
        if (DbTools.executor == null) {
            DbTools.template = npjt;
            DbTools.executor = new JdbcTemplateExecutor(DbTools.template);
        }
    }


    public static void setExecutor(JdbcTemplateExecutor executor) {
        DbTools.executor = executor;
    }

    //获取全局翻页生成器---------------------------
    public static PageSql getPageSql() {
        if (DbTools.pageSql == null)
            if (database.equalsIgnoreCase("mysql")) {
                pageSql = new MysqlPageSql();
            }

        return DbTools.pageSql;
    }

    public static PageSql getPageSqlNormal() {
        if (DbTools.pageSqlNormal == null)
            if (database.equalsIgnoreCase("mysql")) {
                pageSqlNormal = new MysqlPageSql();
            }

        return DbTools.pageSqlNormal;
    }

    public static PageSql getPageSqlById() {
        if (DbTools.pageSqlById == null)
            if (database.equalsIgnoreCase("mysql")) {
                pageSqlById = new MysqlPageSqlById();
            }

        return DbTools.pageSqlById;
    }

    public static PageSql getPageSqlByStartId() {
        if (DbTools.pageSqlByStartId == null)
            if (database.equalsIgnoreCase("mysql")) {
                pageSqlByStartId = new MysqlPageSqlByStartId();
            }

        return DbTools.pageSqlByStartId;
    }


    public static void setPageSql(PageSql pageSql) {
        DbTools.pageSql = pageSql;
    }


    public static NamedParameterJdbcTemplate getTemplate() {
        return template;
    }

    public static void setTemplate(NamedParameterJdbcTemplate template) {
        DbTools.template = template;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getConnection());
    }

    //关闭数据源
    public static void close(Connection conn, DataSource dataSource) {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }

    //获取数据源
    public static DataSource getDataSource() {
        if (dataSource != null)
            return dataSource;

        Properties pro = new Properties();

        InputStream in = DbTools.class.getClassLoader().getResourceAsStream("druid.properties");
        try {
            pro.load(in);
            String db = pro.getProperty("database");
            if (db != null)
                database = db;
            dataSource = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    public static void close(Connection conn, Statement stat, ResultSet res) {
        if (res != null) {
            try {
                res.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}
