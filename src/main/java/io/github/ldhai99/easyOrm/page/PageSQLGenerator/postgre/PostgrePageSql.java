package io.github.ldhai99.easyOrm.page.PageSQLGenerator.postgre;

import io.github.ldhai99.easyOrm.SQL;
import io.github.ldhai99.easyOrm.page.PageModel;
import io.github.ldhai99.easyOrm.page.PageSQLGenerator.PageSQLGenerator;

public class PostgrePageSql implements PageSQLGenerator {
    @Override
    public SQL generatePageSQL(PageModel pageModel, SQL sql) {
        return  sql.clone().last("limit :records offset :start ")
                .setParameter$("start",pageModel.getPageStartRow())
                .setParameter$("records",pageModel.getSize());
    }



}
