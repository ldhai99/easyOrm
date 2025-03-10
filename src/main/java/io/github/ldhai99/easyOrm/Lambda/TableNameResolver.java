package io.github.ldhai99.easyOrm.Lambda;

public class TableNameResolver {

    public static String getTableName(Class<?> clazz) {
        // Check if the class is annotated with @Table
//        if (clazz.isAnnotationPresent(Table.class)) {
//            Table tableAnnotation = clazz.getAnnotation(Table.class);
//            return tableAnnotation.value();
//        } else {
            // If no annotation, convert class name to snake_case as a fallback
            String className = clazz.getSimpleName();
            return toSnakeCase(className);
       // }
    }

    private static String toSnakeCase(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if (Character.isUpperCase(ch) && i > 0) {
                result.append("_");
            }
            result.append(Character.toLowerCase(ch));
        }
        return result.toString();
    }
}