package com.github.ldhai99.easyOrm.easyOrm.jdbcTemplete;


import com.github.ldhai99.easyOrm.BaseMapper;
import com.github.ldhai99.easyOrm.SQL;
import com.github.ldhai99.easyOrm.easyOrm.Student;


import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


import java.sql.SQLException;
import java.util.Date;



public class SelectTest {

    private BaseMapper mapper;

    private NamedParameterJdbcTemplate template;

    @Test
    public void mapperTest() {

        System.out.println(
                mapper.maps(SQL
                        .SELECT("student").eq("age", 18))
        );
        System.out.println(
                mapper.map(SQL
                        .SELECT("student").eq("age", 18))
        );

        System.out.println(
                mapper.oneLongs(SQL.
                        SELECT("student").count().eq("age", 18))
        );
        System.out.println(
                mapper.oneLong(SQL.
                        SELECT("student").sum("age").eq("age", 18))
        );


        mapper.update(SQL.
                UPDATE("student").set("age", 88).eq("name", "李四"));
        mapper.delete(SQL.
                DELETE("student").eq("name", "李四"));

        mapper.insert(SQL.
                INSERT("student")
                .set("id", 0).set("age", 19)
                .set("name", "李四").set("sex", "女")
                .set("student_id", "20190102")
                .set("password", "666").set("create_time", new Date()));

        mapper.update(SQL.
                UPDATE("student")
                .set("age", 28)
                .eq("name", "李四"));

        System.out.println(
                mapper.maps(SQL.
                        SELECT("student").eq("name", "李四"))
        );
    }

    @Test
    public void SQLTest() throws SQLException {
        System.out.println(
                new SQL(template)
                        .select("student")
                        .eq("name", "李四")
                        .getMaps());

        new SQL(template)
                .delete("student")
                .eq("name","李四")
                .execute();
        new SQL(template)
                .insert("student")
                .set("id",0).set("age",19)
                .set("name","李四").set("sex","女")
                .set("student_id","20190102")
                .set("password","666").set("create_time",new Date())
                .execute();
        new SQL(template)
                .update("student")
                .set("age", 99)
                .eq("name", "李四")
                .execute();

        System.out.println(
                new SQL(template).select("student")
                        .eq("name", "李四")
                        .getMaps());
    }
    @Test
    public void SqlDirectTest() throws SQLException{
        Student student = null;
        try {
            student = new SQL(template).select("student")
                    .eq("name","李四").getBean(Student.class);
            System.out.println(student);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        new SQL(template)
                .setSql("delete from student where name=:name")
                .set("name","李四")
                .execute();

        new SQL(template)
                .setSql("insert into student(id,name,password,age,sex,student_id,create_time) " +
                        "values(:id,:name,:password,:age,:sex,:student_id,:create_time)")
                .set("id",0).set("age",19)
                .set("name","李四").set("sex","女")
                .set("student_id","20190102")
                .set("password","666").set("create_time",new Date())
                .execute();


        student = new SQL(template).select("student")
                .eq("name","李四").getBean(Student.class);
        System.out.println(student);

        new SQL(template)
                .setSql("update student set age=:age   where name=:name")
                .set("age","18")
                .set("name","李四")
                .execute();
        student = new SQL(template).select("student")
                .eq("name","李四").getBean(Student.class);
        System.out.println(student);

    }

}