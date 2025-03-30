package io.github.ldhai99.easyOrm.tools;

import io.github.ldhai99.easyOrm.page.PageSQLGenerator.mysql.MysqlPageSql;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.mysql.MysqlPageSqlById;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.mysql.MysqlPageSqlByStartId;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.PageSQLGenerator;

public class ConfigPageSql {
    //全局默认翻页语句生成器
    public static PageSQLGenerator pageSql;
    public static PageSQLGenerator pageSqlNormal;
    public static PageSQLGenerator pageSqlById;
    public static PageSQLGenerator pageSqlByStartId;

    public static PageSQLGenerator getPageSqlNormal() {
        if (pageSqlNormal == null)
            if (ConfigDatabase.database.equalsIgnoreCase("mysql")) {
                pageSqlNormal = new MysqlPageSql();
            }

        return pageSqlNormal;
    }

    public static PageSQLGenerator getPageSqlById() {
        if (pageSqlById == null)
            if (ConfigDatabase.database.equalsIgnoreCase("mysql")) {
                pageSqlById = new MysqlPageSqlById();
            }

        return pageSqlById;
    }

    public static PageSQLGenerator getPageSqlByStartId() {
        if (pageSqlByStartId == null)
            if (ConfigDatabase.database.equalsIgnoreCase("mysql")) {
                pageSqlByStartId = new MysqlPageSqlByStartId();
            }

        return pageSqlByStartId;
    }


    //获取全局翻页生成器---------------------------
    public static PageSQLGenerator getPageSql() {
        if (pageSql == null)
            if (ConfigDatabase.database.equalsIgnoreCase("mysql")) {
                pageSql = new MysqlPageSql();
            }

        return pageSql;
    }

    public static void setPageSql(PageSQLGenerator pageSql) {
        ConfigPageSql.pageSql = pageSql;
    }

    public static void setPageSqlNormal(PageSQLGenerator pageSqlNormal) {
        ConfigPageSql.pageSqlNormal = pageSqlNormal;
    }

    public static void setPageSqlById(PageSQLGenerator pageSqlById) {
        ConfigPageSql.pageSqlById = pageSqlById;
    }

    public static void setPageSqlByStartId(PageSQLGenerator pageSqlByStartId) {
        ConfigPageSql.pageSqlByStartId = pageSqlByStartId;
    }
}
