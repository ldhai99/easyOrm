package io.github.ldhai99.easyOrm.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlTools {
    /**
     * 解析排序子句中的 column 方向（若 column 未参与排序则抛出异常）
     * @param column 分页主键字段名（如 "id"）
     * @param orderByClause ORDER BY 子句（如 "id ASC, name DESC"）
     * @return 是否为升序
     * @throws IllegalArgumentException 如果 countId 未参与排序
     */
    public static boolean parseOrderDirection(String column, String orderByClause) {
        // 正则匹配 countId 的排序方向（不区分大小写）
        Pattern pattern = Pattern.compile(
                "(?i)\\b" + column + "\\s+(ASC|DESC)\\b"
        );
        Matcher matcher = pattern.matcher(orderByClause);

        if (matcher.find()) {
            return "ASC".equalsIgnoreCase(matcher.group(1));
        } else {
            throw new IllegalArgumentException(
                    "分页主键字段 [" + column + "] 必须参与排序，当前 ORDER BY 子句: " + orderByClause
            );
        }
    }

    public static  void main(String[] args){
        // 测试驼峰转下划线
        System.out.println(SqlTools.camelToSnakeCase("userId"));       // 输出: user_id
        System.out.println(SqlTools.camelToSnakeCase("userName"));     // 输出: user_name
        System.out.println(SqlTools.camelToSnakeCase("isActiveUser")); // 输出: is_active_user

        // 测试下划线转驼峰
        System.out.println(SqlTools.snakeToCamelCase("user_id"));       // 输出: userId
        System.out.println(SqlTools.snakeToCamelCase("user_name"));     // 输出: userName
        System.out.println(SqlTools.snakeToCamelCase("is_active_user")); // 输出: isActiveUser

        // 边界情况
        System.out.println(SqlTools.camelToSnakeCase(""));              // 输出: ""
        System.out.println(SqlTools.snakeToCamelCase(""));              // 输出: ""
        System.out.println(SqlTools.camelToSnakeCase(null));            // 输出: null
        System.out.println(SqlTools.snakeToCamelCase(null));            // 输出: null
    }
    /**
     * 将驼峰命名转换为下划线命名
     * @param camelCaseString 驼峰格式的字符串
     * @return 下划线格式的字符串
     */
    public static String camelToSnakeCase(String camelCaseString) {
        if (camelCaseString == null || camelCaseString.isEmpty()) {
            return camelCaseString; // 空字符串直接返回
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCaseString.length(); i++) {
            char currentChar = camelCaseString.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                if (i > 0) {
                    result.append("_"); // 在大写字母前添加下划线
                }
                result.append(Character.toLowerCase(currentChar));
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }

    /**
     * 功能：下划线命名转驼峰命名
     * 将下划线替换为空格,将字符串根据空格分割成数组,再将每个单词首字母大写
     */
    public static String snakeToCamelCase(String snakeCaseString) {
        if (snakeCaseString == null || snakeCaseString.isEmpty()) {
            return snakeCaseString; // 空字符串直接返回
        }
        StringBuilder result = new StringBuilder();
        String[] parts = snakeCaseString.toLowerCase().split("_");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isEmpty()) {
                continue; // 跳过多余的下划线
            }
            if (i == 0) {
                result.append(parts[i]); // 第一个单词保持小写
            } else {
                result.append(capitalize(parts[i])); // 后续单词首字母大写
            }
        }
        return result.toString();
    }

    /**
     * 将字符串首字母大写
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    public static String trim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("^[　 ]+|[　 ]+$","");
        }
    }
    /*去左空格*/
    public static String leftTrim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("^[　 ]+", "");
        }
    }
    /*去右空格*/
    public static String rightTrim(String str) {
        if (str == null || str.equals("")) {
            return str;
        } else {
            return str.replaceAll("[　 ]+$", "");
        }
    }
}
