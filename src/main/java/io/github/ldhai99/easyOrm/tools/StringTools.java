package io.github.ldhai99.easyOrm.tools;

public class StringTools {

    public static  void main(String[] args){
        // 测试驼峰转下划线
        System.out.println(StringTools.camelToSnakeCase("userId"));       // 输出: user_id
        System.out.println(StringTools.camelToSnakeCase("userName"));     // 输出: user_name
        System.out.println(StringTools.camelToSnakeCase("isActiveUser")); // 输出: is_active_user

        // 测试下划线转驼峰
        System.out.println(StringTools.snakeToCamelCase("user_id"));       // 输出: userId
        System.out.println(StringTools.snakeToCamelCase("user_name"));     // 输出: userName
        System.out.println(StringTools.snakeToCamelCase("is_active_user")); // 输出: isActiveUser

        // 边界情况
        System.out.println(StringTools.camelToSnakeCase(""));              // 输出: ""
        System.out.println(StringTools.snakeToCamelCase(""));              // 输出: ""
        System.out.println(StringTools.camelToSnakeCase(null));            // 输出: null
        System.out.println(StringTools.snakeToCamelCase(null));            // 输出: null
    }
    /**
     * 功能：驼峰命名转下划线命名
     * 小写和大写紧挨一起的地方,加上分隔符,然后全部转小写
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
