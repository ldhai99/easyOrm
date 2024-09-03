package io.github.ldhai99.easyOrm.tools;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DbTools {
    /**
     * 获取指定表的所有列名。
     *
     * @param connection 数据库连接
     * @param tableName  表名
     * @return 列名列表
     * @throws SQLException 如果发生 SQL 错误
     */
    public static List<String> getColumnListNames(Connection connection, String tableName) throws SQLException {
        List<String> columnNames = new ArrayList<>();

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columns = metaData.getColumns(null, null, tableName, null);

        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            columnNames.add(columnName);
        }

        return columnNames;
    }
    //用于排除主键
    public static List<String> getColumnListNamesExcept(Connection connection, String tableName,String fieled) throws SQLException {
        List<String> columnNames = getColumnListNames(connection,tableName);;

        return listExcept(columnNames,fieled);

    }

    public static String getColumnNames(Connection connection, String tableName) throws SQLException {
        List<String> columnNames=getColumnListNames(connection,tableName);
        return listToString(columnNames);
    }
    public static String getColumnNamesExcept(Connection connection, String tableName,String field) throws SQLException {
        List<String> columnNames=getColumnListNamesExcept(connection,tableName,field);
        return  listToString(columnNames);
    }
    //list转换为字符串
    public static String listToString(List<String> columnNames) throws SQLException {

        // 使用 StringBuilder 和循环将列表转换为逗号分隔的字符串
        StringBuilder commaSeparatedStringBuilder = new StringBuilder();
        for (int i = 0; i < columnNames.size(); i++) {
            commaSeparatedStringBuilder.append(columnNames.get(i));
            if (i < columnNames.size() - 1) {
                commaSeparatedStringBuilder.append(", ");
            }
        }
        return commaSeparatedStringBuilder.toString();
    }

    public static List<String> listExcept(List<String> columnNames, String fieled) throws SQLException {

        Iterator<String> iterator = columnNames.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            if (element.equals(fieled)) {
                iterator.remove();
            }
        }
        return columnNames;
    }
}
