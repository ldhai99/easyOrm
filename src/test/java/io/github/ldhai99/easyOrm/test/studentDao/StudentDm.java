package io.github.ldhai99.easyOrm.test.studentDao;

import io.github.ldhai99.easyOrm.datamodel.BaseDm;

public class StudentDm extends BaseDm {
    public StudentDm() {
        this.update_table="student";
        this.select_table="student";

        this.table_id="id";
        this.update_fields="student_id, name, password, sex, age";



    }
}
