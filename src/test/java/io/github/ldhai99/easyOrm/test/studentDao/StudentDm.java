package io.github.ldhai99.easyOrm.test.studentDao;

import io.github.ldhai99.easyOrm.datamodel.BaseDm;

public class StudentDm extends BaseDm {
    public StudentDm() {
        this.updateTable="student";
        this.selectTable="student";

        this.tableId="id";
        this.updateFields="student_id, name, password, sex, age";



    }
}
