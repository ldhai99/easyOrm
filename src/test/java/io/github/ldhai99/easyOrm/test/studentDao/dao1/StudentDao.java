package io.github.ldhai99.easyOrm.test.studentDao.dao1;

import io.github.ldhai99.easyOrm.dao.BaseDao;
import io.github.ldhai99.easyOrm.test.studentDao.StudentDm;


public class StudentDao extends BaseDao<StudentDm> {


    public static void main(String[] args) {
        System.out.println(new StudentDao().getDm());
        // System.out.println(new StudentDao(new StudentDm()).getDm());
    }
}