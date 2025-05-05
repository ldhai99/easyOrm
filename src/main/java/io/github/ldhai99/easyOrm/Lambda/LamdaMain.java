package io.github.ldhai99.easyOrm.Lambda;

import io.github.ldhai99.easyOrm.dao.core.FieldResolver;

// 测试用例
public class LamdaMain {
    public static void main(String[] args) throws Exception {
        String property = FieldResolver.field( Student::getName);
        System.out.println(property); // 输出 name
         property = FieldResolver.fullField( Student::getName);
        System.out.println(property); // 输出 name
    }
}