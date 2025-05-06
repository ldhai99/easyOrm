package io.github.ldhai99.easyOrm.test.empty;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;
import io.github.ldhai99.easyOrm.test.Student;
public class EmptyTest {
    @Test
    public void isNull(){

        System.out.println(
               SQL.SELECT("student").column("name,age").isNull(Student::getName).getMaps()
        );

    }
    @Test
    public void isNullValue(){

        System.out.println(
                SQL.SELECT("student").column("name,age").isNull(Student::getName,null).getMaps()
        );

    }
    @Test
    public void isNullValueNotNull(){

        System.out.println(
                SQL.SELECT("student").column("name,age").isNull(Student::getName,"张三").getMaps()
        );

    }
    @Test
    public void isEmptyElseEq(){

        System.out.println(
                SQL.SELECT("student").column("name,age").isEmptyElseEq(Student::getName,"李四").getMaps()
        );
        System.out.println(
                SQL.SELECT("student").column("name,age").isEmptyElseEq(Student::getName,"").getMaps()
        );


    }
}
