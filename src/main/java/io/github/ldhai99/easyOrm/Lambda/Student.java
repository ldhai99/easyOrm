package io.github.ldhai99.easyOrm.Lambda;

// 假设的实体类
class Student implements Cloneable {


    // ID
    private Integer id;
    // 学号
    private String studentId;
    // 登录密码
    private String password;
    // 姓名
    private String name;

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

    // 性别
    private String sex;
}
