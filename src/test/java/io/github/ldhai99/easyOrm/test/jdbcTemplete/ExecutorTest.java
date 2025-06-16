package io.github.ldhai99.easyOrm.test.jdbcTemplete;


import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.core.DynamicSQL;
import io.github.ldhai99.easyOrm.test.Student;


import io.github.ldhai99.easyOrm.executor.Executor;
import io.github.ldhai99.easyOrm.tools.DbTools;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


import java.sql.SQLException;
import java.util.Date;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExecutorTest {

    private Executor executor;

    private NamedParameterJdbcTemplate npjt;

    @BeforeAll
    public void getTemplate() {
        executor = DbTools.getExecutor();

    }

    @Test
    public void npjtTest() {

        //构造器与执行器分开
        System.out.println(
                executor.getMaps(SQL
                        .SELECT("student").eq("age", 18))
        );
        //构造器与执行器合并在一起
        //静态实例化时候传入执行器
        System.out.println(
                SQL.ofExecutor(executor)
                        .select("student").eq("age", 18).getMaps());
        System.out.println(
                SQL.ofExecutor(DbTools.getDataSource())
                        .select("student").eq("age", 18).getMaps());
        System.out.println(
                SQL.ofExecutor(DbTools.getConnection())
                        .select("student").eq("age", 18).getMaps());


        //实例化时候传入执行器
        System.out.println(
                new SQL(executor)
                        .select("student").eq("age", 18).getMaps());
        System.out.println(
                new SQL(DbTools.getDataSource())
                        .select("student").eq("age", 18).getMaps());
        System.out.println(
                new SQL(DbTools.getConnection())
                        .select("student").eq("age", 18).getMaps());


        //实例化后传入执行器
        System.out.println(
                SQL.SELECT("student").eq("age", 18).executor(executor).getMaps());
        System.out.println(
                SQL.SELECT("student").eq("age", 18).executor(DbTools.getDataSource()).getMaps());
        System.out.println(
                SQL.SELECT("student").eq("age", 18).executor(DbTools.getConnection()).getMaps());


        //使用全局默认执行器
        System.out.println(
                SQL.SELECT("student").eq("age", 18).getMaps());

    }

    @Test
    public void mapperTest() {

        System.out.println(
                executor.getMaps(SQL
                        .SELECT("student").eq("age", 18))
        );
        System.out.println(
                executor.getMap(SQL
                        .SELECT("student").eq("age", 18))
        );

        System.out.println(
                executor.getLongs(SQL.
                        SELECT("student").count().eq("age", 18))
        );
        System.out.println(
                executor.getLong(SQL.
                        SELECT("student").sum("age").eq("age", 18))
        );


        executor.update(SQL.
                UPDATE("student").set("age", 88).eq("name", "李四"));
        executor.delete(SQL.
                DELETE("student").eq("name", "李四"));

        executor.insert(SQL.
                INSERT("student")
                .set("id", 0).set("age", 19)
                .set("name", "李四").set("sex", "女")
                .set("student_id", "20190102")
                .set("password", "666").set("create_time", new Date()));

        executor.update(SQL.
                UPDATE("student")
                .set("age", 28)
                .eq("name", "李四"));

        System.out.println(
                executor.getMaps(SQL.
                        SELECT("student").eq("name", "李四"))
        );
    }

    @Test
    public void SQLTest() throws SQLException {
        System.out.println(
                new SQL(npjt)
                        .select("student")
                        .eq("name", "李四")
                        .getMaps());

        new SQL(npjt)
                .delete("student")
                .eq("name", "李四")
                .execute();
        new SQL(npjt)
                .insert("student")
                .set("id", 0).set("age", 19)
                .set("name", "李四").set("sex", "女")
                .set("student_id", "20190102")
                .set("password", "666").set("create_time", new Date())
                .execute();
        new SQL(npjt)
                .update("student")
                .set("age", 99)
                .eq("name", "李四")
                .execute();

        System.out.println(
                new SQL(npjt).select("student")
                        .eq("name", "李四")
                        .getMaps());
    }

    @Test
    public void SqlDirectTest() throws SQLException {
        Student student = null;
        try {
            student = (Student) new SQL(npjt).select("student")
                    .eq("name", "李四").getBean(Student.class);
            System.out.println(student);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        new DynamicSQL(npjt)
                .append("delete from student where name=:name")
                .setParameter("name", "李四")
                .execute();

        new DynamicSQL(npjt)
                .append("insert into student(id,name,password,age,sex,student_id,create_time) " +
                        "values(:id,:name,:password,:age,:sex,:student_id,:create_time)")
                .setParameter("id", 0).setParameter("age", 19)
                .setParameter("name", "李四").setParameter("sex", "女")
                .setParameter("student_id", "20190102")
                .setParameter("password", "666").setParameter("create_time", new Date())
                .execute();


        student = (Student)new SQL(npjt).select("student")
                .eq("name", "李四").getBean(Student.class);
        System.out.println(student);

        new DynamicSQL(npjt)
                .append("update student set age=:age   where name=:name")
                .setParameter("age", "18")
                .setParameter("name", "李四")
                .execute();
        student = (Student)new SQL(npjt).select("student")
                .eq("name", "李四").getBean(Student.class);
        System.out.println(student);

    }

}