package io.github.ldhai99.easyOrm.service.test;

import io.github.ldhai99.easyOrm.dao.orm.EntityMetaMapper;
import io.github.ldhai99.easyOrm.service.test.entity.Person;

import java.util.Map;

public class PropertyToColumnTest {
    // 测试
    public static void main(String[] args) {
        Map<String, String> mapping = EntityMetaMapper.propertyToColumn(Person.class);
        System.out.println(mapping);
    }
}
