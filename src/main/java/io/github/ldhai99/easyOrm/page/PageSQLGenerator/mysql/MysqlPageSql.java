package io.github.ldhai99.easyOrm.page.PageSQLGenerator.mysql;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageModel;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.PageSQLGenerator;

public class MysqlPageSql implements PageSQLGenerator {
    @Override
    public SQL generatePageSQL(PageModel pageModel, SQL sql) {
        return  sql.clone().last("limit :start, :records")
                .setValue$("start",pageModel.getPageStartRow())
                .setValue$("records",pageModel.getSize());
    }

}
