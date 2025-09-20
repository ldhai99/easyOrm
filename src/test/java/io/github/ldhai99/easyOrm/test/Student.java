 package io.github.ldhai99.easyOrm.test;

 import io.github.ldhai99.easyOrm.annotation.TableField;
 import io.github.ldhai99.easyOrm.annotation.Table;

 import java.util.Date;

 //生成get/set/toString/hash/equals等

//开启链式调用，即Student user = new Student().setId(1).setName("张三").setAge(88);
 //@Accessors(chain = true)
 @Table("student")
 public class Student implements  Cloneable{


	 // ID
	 private Integer id;
	 // 学号
	 private String studentId;
	 // 登录密码
	 private String password;
	 // 姓名
	 private String name;
	 // 性别
	 @TableField("sex")
	 private String sex;
	 // 性别
	 private int age;
	 //创建时间
	 @TableField("create_time")
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
//	 @Override
//	 protected Object clone() throws CloneNotSupportedException {
//		 Student student=(Student)super.clone();
//		 student.setCreateTime((Date) this.getCreateTime().clone());
//		 return student;
//	 }

	 public static void main(String[] args) throws CloneNotSupportedException, InterruptedException {
		 Student s1=new Student();
		 s1.setId(1);
		 s1.setName("zhangsan");
		 s1.setCreateTime(new Date());
		 Student s2=(Student) s1.clone();

		 s1.setId(2);
		 System.out.println(s1.getId());
		 System.out.println(s2.getId());

		 s1.setName("李四");
		 System.out.println(s1.getName());
		 System.out.println(s2.getName());
		 Thread.sleep(2000);
		 s1.setCreateTime(new Date());
		 System.out.println(s1.getCreateTime());
		 System.out.println(s2.getCreateTime());

	 }
 }

