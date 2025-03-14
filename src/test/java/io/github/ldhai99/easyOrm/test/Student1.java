package io.github.ldhai99.easyOrm.test;

import java.util.Date;

public class Student1 implements  Cloneable{

    // ID
    private Integer id;
    // 学号
    private String studentId;
    // 登录密码
    private String password;
    // 姓名
    private String name;
    // 性别
    private String sex;
    // 性别
    private int age;
    //创建时间
    private Date createTime;

    public String toString() {
        return "Student(id=" + this.getId() + ", studentId=" + this.getStudentId() + ", password=" + this.getPassword() + ", name=" + this.getName() + ", sex=" + this.getSex() + ", age=" + this.getAge() + ", createTime=" + this.getCreateTime() + ")";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
