package io.github.ldhai99.easyOrm.dao.core;
import io.github.ldhai99.easyOrm.annotation.TableName;
import io.github.ldhai99.easyOrm.tools.SqlTools;

public class TableNameResolver {

    public static String getTableName(Class<?> clazz) {
        // Check if the class is annotated with @Table
        if (clazz.isAnnotationPresent(TableName.class)) {
            TableName tableAnnotation = clazz.getAnnotation(TableName.class);
            return tableAnnotation.value();
        } else {
            // If no annotation, convert class name to snake_case as a fallback
            String className = clazz.getSimpleName();
            return SqlTools.camelToSnakeCase(className);
       }
    }

}