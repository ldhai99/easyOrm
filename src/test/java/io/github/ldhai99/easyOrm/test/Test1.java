package io.github.ldhai99.easyOrm.test;

import io.github.ldhai99.easyOrm.SQL;
import org.junit.jupiter.api.Test;

public class Test1 {
    @Test

    public void aa(){
        System.out.println(SQL.SELECT("student").column("*").getMaps());

    }
}
