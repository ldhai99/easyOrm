package io.github.ldhai99.easyOrm.page.PageSQLGenerator.postgre;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageModel;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.PageSQLGenerator;

public class PostrePageSqlById implements PageSQLGenerator {
    @Override
    public SQL generatePageSQL(PageModel pageModel, SQL sql) {
        return sql.clone().setWhere("").in(pageModel.getCountId(),
                SQL.SELECT(sql.clone().setColumn(pageModel.getCountId())
                                        .last("limit :records offset :start")
                                        .setValue$("start", pageModel.getPageStartRow())
                                        .setValue$("records", pageModel.getSize())
                                , "a")
                        .column("id"));
    }

}
