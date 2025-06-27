CREATE DATABASE `test` DEFAULT CHARACTER SET utf8mb4 ;
    drop table student;
CREATE TABLE student (
  id BIGINT(20) NOT NULL  COMMENT '主键',
  student_id varchar(255) DEFAULT NULL COMMENT '学号',
  name varchar(255) DEFAULT NULL COMMENT '姓名',
  password varchar(255) DEFAULT NULL COMMENT '登录密码',
  sex varchar(255) DEFAULT NULL COMMENT '性别',
  age int(11) DEFAULT NULL COMMENT '年龄',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


-- 插入数据
INSERT  INTO student(id,student_id,name,password,sex,age, create_time)VALUES
(1,'20190101','张三','666','男',19,'2021-01-22 09:48:00'),
(2,'20190102','李四','888','女',18,'2021-01-22 09:48:00'),
(3,'20190103','王五','666','男',20,'2021-01-22 09:48:00'),
(4,'20190104','赵六','888','女',18,'2021-01-22 09:48:00'),
(5,'20190105','李七','888','女',17,'2021-01-22 09:48:00');


CREATE TABLE student1 (
  id BIGINT(20) NOT NULL  AUTO_INCREMENT COMMENT '主键',
  student_id varchar(255) DEFAULT NULL COMMENT '学号',
  name varchar(255) DEFAULT NULL COMMENT '姓名',
  password varchar(255) DEFAULT NULL COMMENT '登录密码',
  sex varchar(255) DEFAULT NULL COMMENT '性别',
  age int(11) DEFAULT NULL COMMENT '年龄',
  create_time DATETIME DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
