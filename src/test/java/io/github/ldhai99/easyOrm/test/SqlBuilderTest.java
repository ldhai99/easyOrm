package io.github.ldhai99.easyOrm.test;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;


public class SqlBuilderTest {
@Test
public  void selectTest(){

    SQL read = new SQL().column("*")
            .column(new SQL().column("age").from("student")
                    .between("age", 1, 100), "age1")

            .from("student")
            .eq("age", 18).or()
            .begin().eq("age", 18).eq("age", 18).end()
            .begin()
            .in("age", new ArrayList<>(Arrays.asList(1, 2, 3))).end()
            .in("age", new SQL().column("age").from("student")
                    .between("age", 1, 100))
            .between("age", 1, 20)
            .between("age", 2, 99)
            .exists(new SQL().column("age").from("student")
                    .between("age", 3, 98));

    System.out.println(read.toString());
}

}
