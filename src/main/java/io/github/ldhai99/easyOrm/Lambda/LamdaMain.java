package io.github.ldhai99.easyOrm.Lambda;

// 测试用例
public class LamdaMain {
    public static void main(String[] args) throws Exception {
        String property = Field.field( Student::getName);
        System.out.println(property); // 输出 name
         property = Field.fullField( Student::getName);
        System.out.println(property); // 输出 name
    }
}