package io.github.ldhai99.easyOrm.service.test;

import io.github.ldhai99.easyOrm.dao.orm.EntityValueMapper;
import io.github.ldhai99.easyOrm.dbenum.UserStatus;
import io.github.ldhai99.easyOrm.service.test.entity.Address;
import io.github.ldhai99.easyOrm.service.test.entity.Gender;
import io.github.ldhai99.easyOrm.service.test.entity.Person;

import java.util.Map;


    // 定义嵌入对象


public class EntityValueMapperTest {
    // 测试代码
    public static void main(String[] args) {
        // 创建测试对象
        Person person = new Person();
        person.setName("John");
        person.setAge(30);
        person.setGender(Gender.MALE);
        person.setStatus(UserStatus.ACTIVE);


       Address address = new Address();
        address.setStreet("Main St");
        address.setCity("New York");
        person.setAddress(address);

        // 获取列值映射
        Map<String, Object> columnValues = EntityValueMapper.toColumnValues(person);

        // 输出结果
        System.out.println(columnValues);
        // 输出: {name=John, age=30, street=Main St, city=New York}
    }
}
