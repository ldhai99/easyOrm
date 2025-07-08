package io.github.ldhai99.easyOrm.tools;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlTools {
    /**
     * 解析排序子句中的 column 方向（若 column 未参与排序则抛出异常）
     *
     * @param column        分页主键字段名（如 "id"）
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

    public static void main(String[] args) {
        System.out.println(SqlTools.isEmpty(0));
        // 测试驼峰转下划线
//        System.out.println(SqlTools.camelToSnakeCase("userId"));       // 输出: user_id
//        System.out.println(SqlTools.camelToSnakeCase("userName"));     // 输出: user_name
//        System.out.println(SqlTools.camelToSnakeCase("isActiveUser")); // 输出: is_active_user

        // 测试下划线转驼峰
//        System.out.println(SqlTools.snakeToCamelCase("user_id"));       // 输出: userId
//        System.out.println(SqlTools.snakeToCamelCase("user_name"));     // 输出: userName
//        System.out.println(SqlTools.snakeToCamelCase("is_active_user")); // 输出: isActiveUser

        // 边界情况
//        System.out.println(SqlTools.camelToSnakeCase(""));              // 输出: ""
//        System.out.println(SqlTools.snakeToCamelCase(""));              // 输出: ""
//        System.out.println(SqlTools.camelToSnakeCase(null));            // 输出: null
//        System.out.println(SqlTools.snakeToCamelCase(null));            // 输出: null
    }

    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param camelCaseString 驼峰格式的字符串
     * @return 下划线格式的字符串
     */
    public static String camelToSnakeCase(String camelCaseString) {
        if (camelCaseString == null || camelCaseString.isEmpty()) {
            return camelCaseString;
        }
        return camelCaseString.replaceAll("([a-z0-9])([A-Z])", "$1_$2")  // 小写/数字 + 大写 → 加下划线
                .replaceAll("([A-Z])([A-Z][a-z])", "$1_$2") // 大写 + 大写+小写 → 加下划线（处理连续大写）
                .toLowerCase();
    }

    /**
     * 功能：下划线命名转驼峰命名
     * 将下划线替换为空格,将字符串根据空格分割成数组,再将每个单词首字母大写
     */
    public static String snakeToCamelCase(String snakeCaseString) {

        if (snakeCaseString == null || snakeCaseString.isEmpty()) {
            return snakeCaseString;
        }
        boolean startsWithUnderscore = snakeCaseString.startsWith("_");
        String[] parts = snakeCaseString.split("_");
        StringBuilder sb = new StringBuilder();
        boolean firstPart = true;
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (firstPart) {
                // 第一个非空部分首字母小写
                sb.append(Character.toLowerCase(part.charAt(0)));
                if (part.length() > 1) {
                    sb.append(part.substring(1).toLowerCase());
                }
                firstPart = false;
            } else {
                // 后续部分首字母大写
                sb.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    sb.append(part.substring(1).toLowerCase());
                }
            }
        }
        // 保留前导下划线
        if (startsWithUnderscore) {
            sb.insert(0, '_');
        }
        return sb.toString();
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }
    /**
     * 检查字符串是否为空
     */
    /***
     * 检查对象是否为空或零值
     *
     * <p>支持以下情况的判断：
     * <ul>
     *   <li>null 值</li>
     *   <li>空字符串、空白字符串（可选）</li>
     *   <li>空集合、空Map</li>
     *   <li>空数组</li>
     *   <li>数值类型的零值（0, 0.0, 0L等）</li>
     *   <li>空的Optional</li>
     *   <li>自定义空对象（通过isEmpty()方法）</li>
     * </ul>
     *
     * @param object 待检查的对象
     * @param trimStrings 是否修剪字符串并检查空白（true：将空白视为空，false：仅检查空字符串）
     * @return true 如果对象为空或零值，false 否则
     */
    public static boolean isEmpty(Object object, boolean trimStrings) {
        if (object == null) {
            return true;
        }

        // 处理字符串
        if (object instanceof String) {
            String str = (String) object;
            return trimStrings ? str.trim().isEmpty() : str.isEmpty();
        }

        // 处理数值类型
        if (object instanceof Number) {
            if (object instanceof Integer) {
                return (Integer) object == 0;
            } else if (object instanceof Long) {
                return (Long) object == 0L;
            } else if (object instanceof Double) {
                return (Double) object == 0.0d;
            } else if (object instanceof Float) {
                return (Float) object == 0.0f;
            } else if (object instanceof BigDecimal) {
                return BigDecimal.ZERO.compareTo((BigDecimal) object) == 0;
            } else if (object instanceof BigInteger) {
                return BigInteger.ZERO.equals(object);
            } else if (object instanceof Short) {
                return (Short) object == 0;
            } else if (object instanceof Byte) {
                return (Byte) object == 0;
            }
            // 其他数值类型
            return ((Number) object).doubleValue() == 0.0;
        }

        // 处理集合
        if (object instanceof Collection) {
            return ((Collection<?>) object).isEmpty();
        }

        // 处理Map
        if (object instanceof Map) {
            return ((Map<?, ?>) object).isEmpty();
        }

        // 处理数组
        if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        }

        // 处理Optional
        if (object instanceof Optional) {
            return !((Optional<?>) object).isPresent();
        }

        // 处理布尔值（可选）
        if (object instanceof Boolean) {
            return !(Boolean) object;
        }

        // 处理自定义空对象（如果对象实现了isEmpty方法）
        try {
            java.lang.reflect.Method method = object.getClass().getMethod("isEmpty");
            if (method.getReturnType() == boolean.class) {
                return (boolean) method.invoke(object);
            }
        } catch (Exception ignored) {
            // 忽略反射异常
        }

        // 默认情况：非空对象
        return false;
    }
    /**
     * 检查对象是否为空或零值（默认不修剪字符串）
     *
     * @param object 待检查的对象
     * @return true 如果对象为空或零值，false 否则
     */
    public static boolean isEmpty(Object object) {
        return isEmpty(object, true);
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
            return str.replaceAll("^[　 ]+|[　 ]+$", "");
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
    // 判断是否为基本/可映射类型
    public static boolean isBasicType(Class<?> type) {
        // 基本类型和包装类
        if (type.isPrimitive() ||
                Number.class.isAssignableFrom(type) ||
                type == Boolean.class ||
                type == Character.class) {
            return true;
        }

        // 常用JDK类型
        return type == String.class ||
                type == Date.class ||
                type == java.sql.Date.class ||
                type == java.sql.Timestamp.class ||
                type == java.time.LocalDate.class ||
                type == java.time.LocalDateTime.class ||
                type == BigDecimal.class ||
                type == byte[].class ||
                type == UUID.class ||
                type.isEnum();
    }
}
