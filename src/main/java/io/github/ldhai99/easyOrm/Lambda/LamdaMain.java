package io.github.ldhai99.easyOrm.Lambda;

// 测试用例
public class LamdaMain {
    public static void main(String[] args) throws Exception {
        String property = LambdaExtractor.getColumnName( Student::getName);
        System.out.println(property); // 输出 name
         property = LambdaExtractor.getFullColumnName( Student::getName);
        System.out.println(property); // 输出 name
    }
}