package io.github.ldhai99.easyOrm.page.PageSQLGenerator.mysql;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageModel;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.PageSQLGenerator;

public class MysqlPageSqlById implements PageSQLGenerator {
    @Override
    public SQL generatePageSQL(PageModel pageModel, SQL sql) {
        return sql.clone().in(pageModel.getCountId(),
                SQL.SELECT(
                        sql.clone().setColumn(pageModel.getCountId())
                                        .last("limit :start, :records")
                                        .setParameter$("start",pageModel.getPageStartRow())
                                        .setParameter$("records",pageModel.getSize())
                                ,"a")
                        .column("id"));
    }

}
