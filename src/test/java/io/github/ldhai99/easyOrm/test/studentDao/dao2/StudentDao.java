package io.github.ldhai99.easyOrm.test.studentDao.dao2;


import io.github.ldhai99.easyOrm.dao.BaseDao;
import io.github.ldhai99.easyOrm.dao.ExplicitConstructor;
import io.github.ldhai99.easyOrm.test.studentDao.StudentDm;


public class StudentDao extends BaseDao<StudentDm> {
    @ExplicitConstructor
    public StudentDao() {
        dm=new StudentDm();
    }
    public StudentDao(StudentDm dm) {
        super(dm);
    }

    public static void main(String[] args) {
        System.out.println(new StudentDao().getDm());
       // System.out.println(new StudentDao(new StudentDm()).getDm());
    }
}
