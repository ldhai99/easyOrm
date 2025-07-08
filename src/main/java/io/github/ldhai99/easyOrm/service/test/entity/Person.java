package io.github.ldhai99.easyOrm.service.test.entity;


import io.github.ldhai99.easyOrm.dbenum.UserStatus;

import java.util.List;

// 定义实体
public class Person {
    public Person() {
    }
    private static int count;  // 1.	静态字段排除
    private transient String temp;  // 2.	瞬态字段排除
    private String name;
    private int age;

    //@Embeddable对象
    private Address address;
    private List<Person> children; // 3.集合类型自动排除


    // 添加枚举字段
    private Gender gender;         // 性别
    //DbEnum类型
    private UserStatus status;

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }



    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Person.count = count;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Person> getChildren() {
        return children;
    }

    public void setChildren(List<Person> children) {
        this.children = children;
    }


}
