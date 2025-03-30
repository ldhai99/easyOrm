package io.github.ldhai99.easyOrm.page.PageSQLGenerator.mysql;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageModel;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.PageSQLGenerator;

public class MysqlPageSqlByStartId implements PageSQLGenerator {
    @Override
    public SQL generatePageSQL(PageModel pageModel, SQL sql) {
        return  sql.clone()
                .gt(pageModel.getCountId(),pageModel.getPageStartId())
                .last(" limit :records")
                .setValue$("records",pageModel.getSize());
    }
}
