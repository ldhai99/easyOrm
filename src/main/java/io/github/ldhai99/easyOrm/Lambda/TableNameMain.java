package io.github.ldhai99.easyOrm.Lambda;

public class TableNameMain {
    public static void main(String[] args) throws Exception {
        String property = TableNameResolver.getTableName( Student.class);
        System.out.println(property); // 输出 name
    }
}
