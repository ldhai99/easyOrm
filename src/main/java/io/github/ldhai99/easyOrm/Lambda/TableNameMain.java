package io.github.ldhai99.easyOrm.Lambda;

import io.github.ldhai99.easyOrm.dao.core.TableNameResolver;

public class TableNameMain {
    public static void main(String[] args) throws Exception {
        String property = TableNameResolver.getTableName( Student.class);
        System.out.println(property); // 输出 name
    }
}
